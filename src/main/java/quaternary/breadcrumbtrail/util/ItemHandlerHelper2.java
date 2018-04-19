package quaternary.breadcrumbtrail.util;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.*;

public class ItemHandlerHelper2 {
	public static int countItems(IItemHandler handler) {
		if(handler == null) return 0;
		
		int runningCount = 0;
		for(int i=0; i < handler.getSlots(); i++) {
			runningCount += handler.getStackInSlot(i).getCount();
		}
		return runningCount;
	}
	
	public static boolean isEmpty(IItemHandler handler) {
		return countItems(handler) == 0;
	}
	
	public static void clear(IItemHandler handler) {
		if(!(handler instanceof IItemHandlerModifiable)) throw new IllegalArgumentException("Can't clear non-IItemHandlerModifiable item handlers");
		
		for(int i=0; i < handler.getSlots(); i++) {
			((IItemHandlerModifiable)handler).setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
	public static Item findFirstItem(IItemHandler handler) {
		for(int i=0; i < handler.getSlots(); i++) {
			if(!handler.getStackInSlot(i).isEmpty()) return handler.getStackInSlot(i).getItem();
		}
		return Items.AIR;
	}
	
	public static ItemStack findFirstItemStack(IItemHandler handler) {
		for(int i=0; i < handler.getSlots(); i++) {
			if(!handler.getStackInSlot(i).isEmpty()) return handler.getStackInSlot(i); 
		}
		return ItemStack.EMPTY;
	}
	
	public static ItemStack extractItem(IItemHandler handler, int count, boolean simulate) {
		ItemStack extractedSoFar = ItemStack.EMPTY;
		int neededCount = count;
		for(int i=0; i < handler.getSlots(); i++) {
			ItemStack extracted = handler.extractItem(i, neededCount, true);
			if(extractedSoFar.isEmpty()) {
				extractedSoFar = extracted.copy();
			} else if(ItemHandlerHelper.canItemStacksStack(extracted, extractedSoFar)) {
				extractedSoFar.grow(extracted.getCount());
			}
			
			if(!simulate) handler.extractItem(i, neededCount, false);
			neededCount -= extracted.getCount();
			
			if(neededCount <= 0) break;
		}
		return extractedSoFar;
	}
}
