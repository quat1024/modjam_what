package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockBreadcrumb extends Block {
	public BlockBreadcrumb() {
		super(Material.CIRCUITS, MapColor.ADOBE);
		
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb");
		setCreativeTab(BreadcrumbTrail.TAB);
		
		setHardness(0.05f);
		setResistance(0.05f);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		IBlockState downState = world.getBlockState(pos.down());
		Block downBlock = downState.getBlock();
		
		Material thisMaterial = world.getBlockState(pos).getMaterial();
		
		return super.canPlaceBlockAt(world, pos) && downBlock.canPlaceTorchOnTop(downState, world, pos.down()) && thisMaterial != Material.WATER && thisMaterial != Material.LAVA;
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
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
