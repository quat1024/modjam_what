package quaternary.breadcrumbtrail.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEndRod;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

public class BlockBreadcrumbGlowing extends BlockBreadcrumbBase {
	public BlockBreadcrumbGlowing() {
		setName("breadcrumb_glowing");
	}
	
	@Override
	public int getDurabilityBarColor() {
		return 0xf4f75b; //yellowish
	}
	
	@Override
	public int getLightValue(IBlockState state) {
		return 6;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(rand.nextInt(3) != 0) return;
		
		double x = .25 + pos.getX() + (rand.nextDouble() * .5);
		double y = pos.getY() + (rand.nextDouble() / 16d) + 0.12;
		double z = .25 + pos.getZ() + (rand.nextDouble() * .5);
		double dx = rand.nextGaussian() * 0.003;
		double dy = rand.nextDouble() * 0.01;
		double dz = rand.nextGaussian() * 0.003;
		
		ParticleEndRodButItCollides p = new ParticleEndRodButItCollides(world, x, y, z, dx, dy, dz);
		p.setColor(0xf4f75b);
		p.setColorFade(0xd4d73b);
		Minecraft.getMinecraft().effectRenderer.addEffect(p);
	}
	
	@SideOnly(Side.CLIENT)
	class ParticleEndRodButItCollides extends ParticleEndRod {
		ParticleEndRodButItCollides(World w, double x, double y, double z, double dx, double dy, double dz) {
			super(w, x, y, z, dx, dy, dz);
			particleScale *= 0.75f; //again
			canCollide = true;
		}
		
		@Override
		public void onUpdate() {
			
			super.onUpdate();
			if(onGround) motionY = 0;
		}
		
		//Copy from Particle#move since ParticleEndRod#move cannot collide
		@Override
		public void move(double x, double y, double z) {
			double d0 = y;
			double origX = x;
			double origZ = z;
			
			if (this.canCollide)
			{
				List<AxisAlignedBB> list = this.world.getCollisionBoxes(null, this.getBoundingBox().expand(x, y, z));
				
				for (AxisAlignedBB axisalignedbb : list)
				{
					y = axisalignedbb.grow(1/20d).calculateYOffset(this.getBoundingBox(), y);
				}
				
				this.setBoundingBox(this.getBoundingBox().offset(0.0D, y, 0.0D));
				
				for (AxisAlignedBB axisalignedbb1 : list)
				{
					x = axisalignedbb1.calculateXOffset(this.getBoundingBox(), x);
				}
				
				this.setBoundingBox(this.getBoundingBox().offset(x, 0.0D, 0.0D));
				
				for (AxisAlignedBB axisalignedbb2 : list)
				{
					z = axisalignedbb2.calculateZOffset(this.getBoundingBox(), z);
				}
				
				this.setBoundingBox(this.getBoundingBox().offset(0.0D, 0.0D, z));
			}
			else
			{
				this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
			}
			
			this.resetPositionToBB();
			this.onGround = d0 != y && d0 < 0.0D;
			
			if (origX != x)
			{
				this.motionX = 0.0D;
			}
			
			if (origZ != z)
			{
				this.motionZ = 0.0D;
			}
		}
	}
}
