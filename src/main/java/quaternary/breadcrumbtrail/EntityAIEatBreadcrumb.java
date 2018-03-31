package quaternary.breadcrumbtrail;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class EntityAIEatBreadcrumb extends EntityAIBase {
	EntityLiving breadEater;
	World world;
	List<BlockPos> cacheNearbyCrumbs;
	
	public EntityAIEatBreadcrumb(EntityLiving breadEater) {
		this.breadEater = breadEater;
		world = breadEater.world;
	}
	
	@Override
	public boolean shouldExecute() {
		if(breadEater.getRNG().nextInt(60) == 0) {
			cacheNearbyCrumbs = findNearbyCrumbs();
			return !cacheNearbyCrumbs.isEmpty();
		} else return false;
	}
	
	@Override
	public void startExecuting() {
		if(cacheNearbyCrumbs.isEmpty()) return; //somehow
		
		breadEater.getNavigator().clearPath();
		BlockPos p = cacheNearbyCrumbs.get(0);
		
		breadEater.getNavigator().tryMoveToXYZ(p.getX(), p.getY(), p.getZ(), 1.6d);
	}
	
	@Override
	public void updateTask() {
		BlockPos crumbPos = breadEater.getPosition();
		if(world.getBlockState(crumbPos).getBlock() instanceof BlockBreadcrumb) {
			world.destroyBlock(crumbPos, false);
		}
	}
	
	private List<BlockPos> findNearbyCrumbs() {
		Iterable<BlockPos.MutableBlockPos> allPositions = BlockPos.getAllInBoxMutable(breadEater.getPosition().add(-10, -5, -10), breadEater.getPosition().add(10, 5, 10));
		ArrayList<BlockPos> breadPos = new ArrayList<>();
		
		for(BlockPos.MutableBlockPos p : allPositions) {
			if(world.getBlockState(p).getBlock() instanceof BlockBreadcrumb) breadPos.add(p.toImmutable());
		}
		
		Collections.shuffle(breadPos);
		return breadPos;
	}
}
