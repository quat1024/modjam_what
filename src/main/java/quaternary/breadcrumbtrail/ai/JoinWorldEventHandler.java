package quaternary.breadcrumbtrail.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.*;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.breadcrumbtrail.BreadcrumbTrail;

@Mod.EventBusSubscriber(modid = BreadcrumbTrail.MODID)
public class JoinWorldEventHandler {
	@SubscribeEvent
	public static void joinWorld(EntityJoinWorldEvent evt) {
		Entity ent = evt.getEntity();
		
		if(ent instanceof EntityLiving) {
			EntityLiving living = (EntityLiving) ent;
			
			if(living instanceof EntityParrot) {
				//btw parrots here have a higher chance because they are a bit slower to move
				//the other animals outrun them all the time!! so let's give them some help
				((EntityParrot) living).tasks.addTask(2, new EntityAIEatBreadcrumb(living, SoundEvents.ENTITY_PARROT_EAT, 20));
			} else if(living instanceof EntityOcelot) {
				((EntityOcelot) living).tasks.addTask(4, new EntityAIEatBreadcrumb(living, null, 50));
			} else if(living instanceof EntityWolf) {
				//mehhh cant think of a good sound
				((EntityWolf) living).tasks.addTask(4, new EntityAIEatBreadcrumb(living, SoundEvents.ENTITY_WOLF_AMBIENT, 40));
			} else if(living instanceof EntityRabbit) {
				//NOTE TO SELF do not use entity_rabbit_attack
				//oh no
				//i can never unhear it
				((EntityRabbit) living).tasks.addTask(4, new EntityAIEatBreadcrumb(living, null, 30));
			}
		}
	}
}
