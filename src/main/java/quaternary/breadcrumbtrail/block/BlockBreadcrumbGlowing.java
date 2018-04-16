package quaternary.breadcrumbtrail.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockBreadcrumbGlowing extends BlockBreadcrumbBase {
	public BlockBreadcrumbGlowing() {
		setName("breadcrumb_glowing");
	}
	
	@Override
	int getDurabilityBarColor() {
		return 0xf4f75b; //yellowish
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return 4;
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(rand.nextInt(4) != 0) return;
		
		double x = pos.getX() + rand.nextDouble();
		double y = pos.getY() + (rand.nextDouble() / 16d);
		double z = pos.getZ() + rand.nextDouble();
		
		world.spawnParticle(EnumParticleTypes.END_ROD, x, y, z, rand.nextGaussian() * 0.001, -rand.nextDouble() * 0.01, rand.nextGaussian() * 0.001);
	}
}
