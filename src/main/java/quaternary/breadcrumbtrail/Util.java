package quaternary.breadcrumbtrail;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Util {	
	//item utils!
	public static void setUpItem(Item i, String regName) {
		i.setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, regName));
		i.setUnlocalizedName(BreadcrumbTrail.MODID + "." + regName);
		i.setCreativeTab(BreadcrumbTrail.TAB);
	}
	
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
	
	public static BlockPos getItemNBTBlockPos(ItemStack stack, String key, BlockPos def) {
		verifyTag(stack);
		if(!hasKey(stack, key)) return def;
		
		NBTTagCompound posTag = stack.getTagCompound().getCompoundTag(key);
		return NBTUtil.getPosFromTag(posTag);
	}
	
	public static void setItemNBTBlockPos(ItemStack stack, String key, BlockPos val) {
		verifyTag(stack);
		
		stack.getTagCompound().setTag(key, NBTUtil.createPosTag(val));
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
		//hmm
		if(crumbs < 0) return I18n.translateToLocal("breadcrumbtrail.crumbcount.hahawhat").replace('&', '\u00A7');
		
		if(crumbs == 0) return I18n.translateToLocal("breadcrumbtrail.crumbcount.zero");
		else if(crumbs == 1) return I18n.translateToLocal("breadcrumbtrail.crumbcount.one");
		else if(crumbs <= 4) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.exactplural", crumbs);
		
		//fight me irl
		String qualifier;
		if (crumbs <= 10) qualifier = "few";
		else if (crumbs <= 25) qualifier = "handful";
		else if (crumbs <= 40) qualifier = "couple";
		else if (crumbs <= 100) qualifier = "fair";
		else if (crumbs <= 150) qualifier = "sizable";
		else if (crumbs <= 300) qualifier = "lot";
		else qualifier = "ton";
		
		return I18n.translateToLocal("breadcrumbtrail.crumbcount." + qualifier);
	}
	
	//hah!
	public static String italicise(String in) {
		return TextFormatting.ITALIC + in + TextFormatting.RESET;
	}
}
