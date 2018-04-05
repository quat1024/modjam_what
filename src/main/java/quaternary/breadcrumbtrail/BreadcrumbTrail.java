package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.breadcrumbtrail.ai.EntityAIEatBreadcrumb;
import quaternary.breadcrumbtrail.block.BlockBreadcrumb;
import quaternary.breadcrumbtrail.item.ItemBreadcrumbPouch;
import quaternary.breadcrumbtrail.recipe.RecipeFillPouch;

import java.util.*;

@Mod(modid = BreadcrumbTrail.MODID, name = BreadcrumbTrail.NAME, version = BreadcrumbTrail.VERSION)
public class BreadcrumbTrail {
	public static final String MODID = "breadcrumbtrail";
	public static final String NAME = "Breadcrumb Trail";
	public static final String VERSION = "1.0.0";
	
	public static List<Block> BLOCKS;
	public static List<Item> ITEMS;
	
	public static final BreadcrumbCreative TAB = new BreadcrumbCreative();
	
	public static final StatBase LEAVE_BREADCRUMB_STAT = new StatBasic("stat.breadcrumbtrail.leavecrumb", new TextComponentTranslation("stat.breadcrumbtrail.leavecrumb")).initIndependentStat().registerStat();
	
	static {
		BLOCKS = new ArrayList<>();
		BLOCKS.add(new BlockBreadcrumb());
		
		ITEMS = new ArrayList<>();
		for(Block b : BLOCKS) {
			ItemBlock ib = new ItemBlock(b);
			//At this point, I start to wonder why I'm doing this.
			//The mod ony adds one block, for crying out loud.
			ib.setRegistryName(b.getRegistryName());
			ITEMS.add(ib);
		}
		
		ITEMS.add(new ItemBreadcrumbPouch());
	}
	
	@GameRegistry.ObjectHolder(MODID + ":breadcrumb")
	public static final Item BREADCRUMB_ITEM = Items.AIR;
	
	@Mod.EventHandler
	public static void postinit(FMLPostInitializationEvent e) {
		Set<Item> parrotTameItems = ReflectionHelper.getPrivateValue(EntityParrot.class, null, "TAME_ITEMS", "field_192016_bJ");
		
		parrotTameItems.add(BREADCRUMB_ITEM);
	}
	
	@Mod.EventBusSubscriber(modid = MODID)
	public static class EventHandler {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			reg.registerAll(BLOCKS.toArray(new Block[0]));
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			reg.registerAll(ITEMS.toArray(new Item[0]));
		}
		
		@SubscribeEvent
		public static void recipes(RegistryEvent.Register<IRecipe> e) {
			IForgeRegistry<IRecipe> reg = e.getRegistry();
			
			reg.register(new RecipeFillPouch());
		}
		
		//But wait, there's more!
		@SubscribeEvent
		public static void joinWorld(EntityJoinWorldEvent evt) {
			Entity ent = evt.getEntity();
			
			if(ent instanceof EntityLiving) {
				EntityLiving living = (EntityLiving) ent;
				
				if(living instanceof EntityParrot) {
					//btw parrots here have a higher chance because they are a bit slower to move
					//the other animals outrun them all the time!! so let's give them some help
					((EntityParrot) living).tasks.addTask(4, new EntityAIEatBreadcrumb(living, SoundEvents.ENTITY_PARROT_EAT, 20));
				} else if(living instanceof EntityOcelot) {
					((EntityOcelot) living).tasks.addTask(4, new EntityAIEatBreadcrumb(living, null, 50));
				} else if(living instanceof EntityWolf) {
					//mehhh cant think of a good sound
					((EntityWolf) living).tasks.addTask(8, new EntityAIEatBreadcrumb(living, SoundEvents.ENTITY_WOLF_AMBIENT, 40));
				} else if(living instanceof EntityRabbit) {
					//NOTE TO SELF do not use entity_rabbit_attack
					//oh no
					//i can never unhear it
					((EntityRabbit) living).tasks.addTask(6, new EntityAIEatBreadcrumb(living, null, 30));
				}
			}
		}
	}
	
	@Mod.EventBusSubscriber(modid = MODID, value = Side.CLIENT)
	public static class ClientEventHandler {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			for(Item i : ITEMS) {
				ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
				ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
			}
		}
	}
}
