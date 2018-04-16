package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.breadcrumbtrail.block.*;
import quaternary.breadcrumbtrail.item.ItemBreadcrumb;
import quaternary.breadcrumbtrail.item.pouch.ItemBreadcrumbPouch;
import quaternary.breadcrumbtrail.item.ItemSimple;
import quaternary.breadcrumbtrail.recipe.RecipeFillPouch;

import java.util.*;

@Mod(modid = BreadcrumbTrail.MODID, name = BreadcrumbTrail.NAME, version = BreadcrumbTrail.VERSION)
public class BreadcrumbTrail {
	public static final String MODID = "breadcrumbtrail";
	public static final String NAME = "Breadcrumb Trail";
	public static final String VERSION = "1.1.0";
	
	public static final BreadcrumbCreative TAB = new BreadcrumbCreative();
	
	public static final StatBase LEAVE_BREADCRUMB_STAT = new StatBasic("stat.breadcrumbtrail.leavecrumb", new TextComponentTranslation("stat.breadcrumbtrail.leavecrumb")).initIndependentStat().registerStat();
	
	public static List<BlockBreadcrumbBase> CRUMBS = new ArrayList<>();
	
	public static List<Block> BLOCKS = new ArrayList<>();
	public static List<Item> ITEMS = new ArrayList<>();
	
	public static final boolean DEBUGEROO = true;
	
	static {
		//do this first so it shows first in creative
		ITEMS.add(new ItemBreadcrumbPouch());
		
		CRUMBS.add(new BlockBreadcrumb());
		CRUMBS.add(new BlockBreadcrumbGlowing());
		
		for(BlockBreadcrumbBase crumb : CRUMBS) {
			BlockBase b = crumb.build();
			BLOCKS.add(b);
			ITEMS.add(new ItemBreadcrumb(b));
		}
		
		ITEMS.add(new ItemSimple("glowstone_fleck"));
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
