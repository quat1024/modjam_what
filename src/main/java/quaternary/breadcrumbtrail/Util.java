package quaternary.breadcrumbtrail;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Util {
	public static boolean getItemNBTBoolean(ItemStack stack, String key, boolean def) {
		verifyTag(stack);
		if(!hasKey(stack, key)) return def;
		
		return stack.getTagCompound().getBoolean(key);
	}
	
	public static void setItemNBTBoolean(ItemStack stack, String key, boolean val) {
		verifyTag(stack);
		
		stack.getTagCompound().setBoolean(key, val);
	}
	
	public static int getItemNBTInt(ItemStack stack, String key, int def) {
		verifyTag(stack);
		if(!hasKey(stack, key)) return def;
		
		return stack.getTagCompound().getInteger(key);
	}
	
	public static void setItemNBTInt(ItemStack stack, String key, int val) {
		verifyTag(stack);
		
		stack.getTagCompound().setInteger(key, val);
	}
	
	static void verifyTag(ItemStack stack) {
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
	}
	
	static boolean hasKey(ItemStack stack, String key) {
		return stack.getTagCompound().hasKey(key);
	}
}
