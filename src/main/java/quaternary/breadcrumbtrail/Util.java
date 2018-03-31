package quaternary.breadcrumbtrail;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Util {
	//item utils!
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
	
	//number formatting utils!
	@SideOnly(Side.CLIENT)
	public static String vagueCrumbCount(int crumbs) {
		if(crumbs == 1) return I18n.translateToLocal("breadcrumbtrail.crumbcount.one");
		else if(crumbs <= 4) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.exactplural", crumbs);
		
		//fight me irl
		String qualifier;
		if (crumbs <= 10) qualifier = "few";
		else if (crumbs <= 25) qualifier = "handful";
		else if (crumbs <= 40) qualifier = "couple";
		else if (crumbs <= 100) qualifier = "fair";
		else if (crumbs <= 300) qualifier = "sizable";
		else if (crumbs <= 700) qualifier = "lot";
		else qualifier = "ton";
		
		return I18n.translateToLocal("breadcrumbtrail.crumbcount." + qualifier);
	}
}
