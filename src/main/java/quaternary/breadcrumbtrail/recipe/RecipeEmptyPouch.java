package quaternary.breadcrumbtrail.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;
import quaternary.breadcrumbtrail.BreadcrumbTrail;
import quaternary.breadcrumbtrail.item.pouch.ItemBreadcrumbPouch;
import quaternary.breadcrumbtrail.util.ItemHandlerHelper2;

public class RecipeEmptyPouch extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
	public RecipeEmptyPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "empty_pouch"));
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World world) {		
		ItemStack bag = ItemStack.EMPTY;
		
		for(int i=0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			
			if(bag.isEmpty() && stack.getItem() instanceof ItemBreadcrumbPouch) {
				bag = stack; continue;
			}
			
			return false;
		}
		
		if(bag.isEmpty() || !ItemBreadcrumbPouch.isOpen(bag)) return false;
		
		IItemHandler handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		return !ItemHandlerHelper2.isEmpty(handler);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack bagClone = ItemStack.EMPTY;
		int bagIndex = -1;
		
		for(int i=0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			
			if(stack.getItem() instanceof ItemBreadcrumbPouch) {
				bagClone = stack.copy();
				bagIndex = i;
				break;
			}
		}
		
		IItemHandler handler = bagClone.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack extractedCrumbs = handler.extractItem(0, 64, false);
		inv.setInventorySlotContents(bagIndex, extractedCrumbs);
		
		return bagClone;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 1;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
