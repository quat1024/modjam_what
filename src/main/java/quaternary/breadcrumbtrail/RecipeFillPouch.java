package quaternary.breadcrumbtrail;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RecipeFillPouch extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	public RecipeFillPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "fill_pouch"));
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@GameRegistry.ObjectHolder(BreadcrumbTrail.MODID + ":breadcrumb")
	public static final Item crumbItem = Items.AIR;
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		ItemStack bag = findBag(inv);
		ItemStack crumbs = findCrumbs(inv);
		
		return !bag.isEmpty() && !crumbs.isEmpty() && ItemBreadcrumbPouch.isOpen(bag);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack bag = findBag(inv).copy();
		ItemStack crumbs = findCrumbs(inv).copy();
		
		if(ItemBreadcrumbPouch.getCrumbs(bag) < ItemBreadcrumbPouch.MAX_CRUMBS) {
			ItemBreadcrumbPouch.setCrumbs(bag, ItemBreadcrumbPouch.getCrumbs(bag) + 1);
		}
		
		return bag;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	private ItemStack findBag(InventoryCrafting inv) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			Item item = stack.getItem();
			
			if(item instanceof ItemBreadcrumbPouch) return stack;
		}
		
		return ItemStack.EMPTY;
	}
	
	private ItemStack findCrumbs(InventoryCrafting inv) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			Item item = stack.getItem();
			
			if(item.equals(crumbItem)) return stack;
		}
		
		return ItemStack.EMPTY;
	}
}
