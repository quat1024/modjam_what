package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = BreadcrumbTrail.MODID, name = BreadcrumbTrail.NAME, version = BreadcrumbTrail.VERSION)
public class BreadcrumbTrail {
	public static final String MODID = "breadcrumbtrail";
	public static final String NAME = "Breadcrumb Trail";
	public static final String VERSION = "1.0.0";
	
	public static List<Block> BLOCKS;
	public static List<Item> ITEMS;
	
	@GameRegistry.ItemStackHolder(MODID + ":breadcrumb_pouch")
	public static final ItemStack tabStack = ItemStack.EMPTY; 
	
	public static final CreativeTabs TAB = new CreativeTabs(MODID) {
		@Override
		public ItemStack getTabIconItem() {
			return tabStack;
		}
	};
	
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
		public static void joinWorld(EntityJoinWorldEvent e) {
			if(e.getEntity() instanceof EntityParrot) {
				EntityParrot parrot = (EntityParrot) e.getEntity();
				parrot.tasks.addTask(10, new EntityAIEatBreadcrumb(parrot));
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
