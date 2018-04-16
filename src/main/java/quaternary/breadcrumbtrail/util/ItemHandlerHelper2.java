package quaternary.breadcrumbtrail.util;

import net.minecraftforge.items.IItemHandler;

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
		for(int i=0; i < handler.getSlots(); i++) {
			handler.extractItem(i, Integer.MAX_VALUE, false); //and throw the value away
		}
	}
}
