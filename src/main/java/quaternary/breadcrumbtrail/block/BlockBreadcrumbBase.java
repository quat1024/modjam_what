package quaternary.breadcrumbtrail.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class BlockBreadcrumbBase extends BlockBase {
	public BlockBreadcrumbBase() {
		super(Material.CIRCUITS, MapColor.ADOBE);
		
		setHardness(0.05f);
		setResistance(0.05f);
		
		setSoundType(SoundType.SNOW);
	}
	
	abstract int getDurabilityBarColor(); //the pouch durability bar color in 0xRRGGBB format
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
		//sanity check
		IBlockState state = world.getBlockState(pos);
		if(!(state.getBlock() instanceof BlockBreadcrumbBase)) return false;
		
		for(int i=0; i < 3; i++) {
			double x = world.rand.nextDouble() + pos.getX();
			double y = 0.05d + pos.getY();
			double z = world.rand.nextDouble() + pos.getZ();
			double dx = (world.rand.nextDouble() - 0.5) / 10;
			double dy = 0.01;
			double dz = (world.rand.nextDouble() - 0.5) / 10;
			
			manager.spawnEffectParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), x, y, z, dx, dy, dz, Block.getIdFromBlock(state.getBlock()));
		}
		
		return true;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && canStay(world, pos);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if(!canStay(world, pos)) {
			world.destroyBlock(pos, true);
		}
	}
	
	private boolean canStay(World world, BlockPos pos) {
		IBlockState downState = world.getBlockState(pos.down());
		Block downBlock = downState.getBlock();
		
		Material thisMaterial = world.getBlockState(pos).getMaterial();
		
		return downBlock.canPlaceTorchOnTop(downState, world, pos.down()) && !thisMaterial.isLiquid();
	}
	
	public static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 2/16d, 1);
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
}
