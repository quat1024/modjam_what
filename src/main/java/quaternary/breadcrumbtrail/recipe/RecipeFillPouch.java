package quaternary.breadcrumbtrail.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	
	ThreadLocal<ItemStack> leftoverStack = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
	ThreadLocal<Integer> leftoverSlot = ThreadLocal.withInitial(() -> -1);
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		ItemStack bagClone = ItemStack.EMPTY;
		ItemStack crumbs = ItemStack.EMPTY;
		int crumbSlot = -1;
		
		for(int i=0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			
			if(bagClone.isEmpty() && stack.getItem() instanceof ItemBreadcrumbPouch) {
				bagClone = stack.copy();
				continue;
			}
			
			if(crumbs.isEmpty() && stack.getItem() instanceof ItemBreadcrumb) {
				crumbs = stack;
				crumbSlot = i;
			}
		}
		
		IItemHandler resultHandler = bagClone.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		ItemStack leftover = resultHandler.insertItem(0, crumbs, false);
		System.out.println(leftover);
		
		leftoverStack.set(leftover);
		leftoverSlot.set(crumbSlot);
		
		return bagClone;
	}
	
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
				
		for (int i = 0; i < ret.size(); i++) {
			if(i == leftoverSlot.get()) {
				ret.set(i, leftoverStack.get());
			}
		}
		return ret;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
