package quaternary.breadcrumbtrail.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.*;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
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
		Triple<Boolean, ItemStack, ItemStack> triple = findBagAndCrumbs(inv);
		
		if(!triple.getLeft()) return false;
		
		ItemStack bag = triple.getMiddle();
		ItemStack crumbs = triple.getRight();
		
		IItemHandler handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(handler == null) return false;
		
		ItemStack leftover = ItemHandlerHelper.insertItem(handler, crumbs, true);
		
		return leftover.isEmpty();
	}
	
	private final Triple<Boolean, ItemStack, ItemStack> INVALID_RECIPE_TRIPLE = Triple.of(false, ItemStack.EMPTY, ItemStack.EMPTY);
	
	private Triple<Boolean, ItemStack, ItemStack> findBagAndCrumbs(InventoryCrafting inv) {
		ItemStack bag = ItemStack.EMPTY;
		ItemStack crumbs = ItemStack.EMPTY;
		
		for(int i=0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			
			if(bag.isEmpty() && stack.getItem() instanceof ItemBreadcrumbPouch && ItemBreadcrumbPouch.isOpen(stack)) {
				bag = stack; continue;
			}
			
			if(stack.getItem() instanceof ItemBreadcrumb) {
				if(crumbs.isEmpty()) {
					ItemStack stack2 = stack.copy();
					stack2.setCount(1);
					crumbs = stack2;
					continue;
				} else if(ItemHandlerHelper.canItemStacksStack(crumbs, stack)) {
					crumbs.grow(1);
					continue;
				}
			}
			
			return INVALID_RECIPE_TRIPLE;
		}
		
		if(bag.isEmpty() || crumbs.isEmpty()) return INVALID_RECIPE_TRIPLE;
		
		return Triple.of(true, bag, crumbs);
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		Triple<Boolean, ItemStack, ItemStack> triple = findBagAndCrumbs(inv);
		
		if(!triple.getLeft()) return ItemStack.EMPTY;
		
		ItemStack bag = triple.getMiddle().copy();
		ItemStack crumbs = triple.getRight();
		
		IItemHandler handler = bag.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(handler == null) return ItemStack.EMPTY;
		
		ItemHandlerHelper.insertItem(handler, crumbs, false);
		return bag;
	}
	
	/*
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
	*/
	
	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
