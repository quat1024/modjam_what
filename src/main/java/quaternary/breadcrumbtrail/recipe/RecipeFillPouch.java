package quaternary.breadcrumbtrail.recipe;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.IForgeRegistryEntry;
import quaternary.breadcrumbtrail.BreadcrumbTrail;
import quaternary.breadcrumbtrail.item.ItemBreadcrumb;
import quaternary.breadcrumbtrail.item.pouch.ItemBreadcrumbPouch;

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
	public boolean matches(InventoryCrafting inv, World world) {
		ItemStack bag = ItemStack.EMPTY;
		ItemStack crumbs = ItemStack.EMPTY;
		
		for(int i=0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			
			if(bag.isEmpty() && stack.getItem() instanceof ItemBreadcrumbPouch) {
				bag = stack; continue;
			}
			
			if(crumbs.isEmpty() && stack.getItem() instanceof ItemBreadcrumb) {
				crumbs = stack; continue;
			}
			
			return false;
		}
		
		if(bag.isEmpty() || crumbs.isEmpty() || !ItemBreadcrumbPouch.isOpen(bag)) return false;
		
		IItemHandler handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		ItemStack leftover = handler.insertItem(0, crumbs, true);
		
		return !ItemStack.areItemStacksEqual(crumbs, leftover);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack result = find(inv, ItemBreadcrumbPouch.class).copy();
		ItemStack crumbs = find(inv, ItemBreadcrumb.class);
		
		IItemHandler resultHandler = result.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		ItemStack leftover = resultHandler.insertItem(0, crumbs, false);
		
		int crumbIndex = findIndex(inv, ItemBreadcrumb.class);
		if(crumbIndex != -1) inv.setInventorySlotContents(crumbIndex, leftover);
		
		return result;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
	
	private ItemStack find(InventoryCrafting inv, Class itemClass) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			Item item = stack.getItem();
			
			if(itemClass.isInstance(item)) return stack;
		}
		
		return ItemStack.EMPTY;
	}
	
	private int findIndex(InventoryCrafting inv, Class itemClass) {
		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			Item item = stack.getItem();
			
			if(itemClass.isInstance(item)) return i;
		}
		
		return -1;
	}
}
