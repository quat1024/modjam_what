package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

public class BlockBreadcrumb extends Block {
	public BlockBreadcrumb() {
		super(Material.CIRCUITS, MapColor.ADOBE);
		
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb");
		
		setHardness(0.1f);
		setResistance(0.05f);
	}
	
	public static final AxisAlignedBB AABB = new AxisAlignedBB(2/16d, 0, 2/16d, 14/16d, 2/16d, 14/16d);
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}
}
