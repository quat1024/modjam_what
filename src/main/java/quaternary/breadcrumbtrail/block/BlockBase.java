package quaternary.breadcrumbtrail.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import quaternary.breadcrumbtrail.BreadcrumbTrail;

public class BlockBase extends Block {
	public BlockBase(Material mat, MapColor color) {
		super(mat, color); 
	}
	
	//Begin spaghet
	String name;
	
	void setName(String name) {
		this.name = name;
	}
	
	public void build() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, name));
		setUnlocalizedName(BreadcrumbTrail.MODID + "." + name);
		setCreativeTab(BreadcrumbTrail.TAB);
	}
	//End spaghet
	
	//majong Pls
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
