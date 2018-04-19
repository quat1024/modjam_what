package quaternary.breadcrumbtrail.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import quaternary.breadcrumbtrail.block.BlockBreadcrumbBase;

import javax.annotation.Nullable;
import java.util.*;

public class EntityAIEatBreadcrumb extends EntityAIBase {
	private EntityLiving breadEater;
	private World world;
	private List<BlockPos> cacheNearbyCrumbs;
	
	//How long I will check whether I am inside a crumb block.
	private int crumbCheckTimer = 0;
	
	//How long until I can eat the crumb after I enter its block space.
	private int eatCrumbTimer = 0;
	
	//Should I instantly look again for crumbs after eating one?
	private boolean immediatelyRetryFlag = false;
	
	SoundEvent eatSound = SoundEvents.ENTITY_GENERIC_EAT;
	private int chance;
	
	public EntityAIEatBreadcrumb(EntityLiving breadEater, @Nullable SoundEvent eatSound, int chance) {
		this.breadEater = breadEater;
		world = breadEater.world;
		
		if(eatSound != null) {
			this.eatSound = eatSound; 
		}
		
		this.chance = chance;
	}
	
	@Override
	public boolean shouldExecute() {
		if(immediatelyRetryFlag || breadEater.getRNG().nextInt(chance) == 0) {
			cacheNearbyCrumbs = findNearbyCrumbs(breadEater.getPosition());
			immediatelyRetryFlag = false;
			return !cacheNearbyCrumbs.isEmpty();
		} else return false;
	}
	
	@Override
	public int getMutexBits() {
		return 0;
	}
	
	@Override
	public void startExecuting() {
		if(cacheNearbyCrumbs.isEmpty()) return; //somehow
		
		if(breadEater instanceof EntityTameable && ((EntityTameable)breadEater).isSitting()) return;
		
		breadEater.getNavigator().clearPath();
		BlockPos p = cacheNearbyCrumbs.get(0);
		
		breadEater.getNavigator().tryMoveToXYZ(p.getX(), p.getY(), p.getZ(), breadEater.getMoveHelper().getSpeed());
		
		crumbCheckTimer = 200; //check for crumbs on the ground for 200 ticks (10 seconds)
	}
	
	@Override
	public boolean shouldContinueExecuting() {
		return crumbCheckTimer > 0 || eatCrumbTimer > 0;
	}
	
	@Override
	public void updateTask() {
		if(!world.getGameRules().getBoolean("mobGriefing")) return;
		BlockPos crumbPos = breadEater.getPosition();
		
		//are we there yet?
		if(crumbCheckTimer > 0) {
			crumbCheckTimer--;
			
			if(crumbCheckTimer % 3 == 0 && isCrumb(world.getBlockState(crumbPos))) {
				eatCrumbTimer = world.rand.nextInt(5) + 7;
				crumbCheckTimer = 0;
				breadEater.getNavigator().clearPath(); //stop moving
			}
			
			return;
		}
		
		//can I eat yet?
		if(eatCrumbTimer > 0) {
			eatCrumbTimer--;
		}
		
		if (eatCrumbTimer == 0 && isCrumb(world.getBlockState(crumbPos))){
			world.destroyBlock(crumbPos, false);
			
			world.playSound(null, crumbPos, eatSound, breadEater.getSoundCategory(), .4f, 1f);
			
			crumbCheckTimer = 0; //done executing task
			immediatelyRetryFlag = true; //look for more crumbs next tick!
		}
	}
	
	private boolean isCrumb(IBlockState state) {
		return state.getBlock() instanceof BlockBreadcrumbBase;
	}
	
	private List<BlockPos> findNearbyCrumbs(BlockPos myPosition) {
		Iterable<BlockPos.MutableBlockPos> allPositions = BlockPos.getAllInBoxMutable(myPosition.add(-10, -5, -10), myPosition.add(10, 5, 10));
		ArrayList<BlockPos> breadPos = new ArrayList<>();
		
		for(BlockPos.MutableBlockPos p : allPositions) {
			if(isCrumb(world.getBlockState(p))) breadPos.add(p.toImmutable());
		}
		
		Collections.shuffle(breadPos);
		
		return breadPos;
	}
}
