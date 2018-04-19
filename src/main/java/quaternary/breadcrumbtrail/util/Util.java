package quaternary.breadcrumbtrail.util;

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
import quaternary.breadcrumbtrail.BreadcrumbTrail;

import java.util.Locale;

public class Util {	
	//item utils!
	public static void setUpItem(Item i, String regName) { //Spaghet
		i.setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, regName));
		i.setUnlocalizedName(BreadcrumbTrail.MODID + "." + regName);
		i.setCreativeTab(BreadcrumbTrail.TAB);
	}
	
	public static void setUpItem(Item i, ResourceLocation res) {
		i.setRegistryName(res);
		i.setUnlocalizedName(res.getResourceDomain() + "." + res.getResourcePath());
		i.setCreativeTab(BreadcrumbTrail.TAB);
	}
	
	public static boolean getItemNBTBoolean(ItemStack stack, String key, boolean def) {
		if(!hasKey(stack, key)) return def;
		
		return stack.getTagCompound().getBoolean(key);
	}
	
	public static void setItemNBTBoolean(ItemStack stack, String key, boolean val) {
		verifyStackTag(stack);
		
		stack.getTagCompound().setBoolean(key, val);
	}
	
	public static BlockPos getItemNBTBlockPos(ItemStack stack, String key, BlockPos def) {
		if(!hasKey(stack, key)) return def;
		
		NBTTagCompound posTag = stack.getTagCompound().getCompoundTag(key);
		return NBTUtil.getPosFromTag(posTag);
	}
	
	public static void setItemNBTBlockPos(ItemStack stack, String key, BlockPos val) {
		verifyStackTag(stack);
		
		stack.getTagCompound().setTag(key, NBTUtil.createPosTag(val));
	}
	
	static void verifyStackTag(ItemStack stack) {
		if(!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
	}
	
	static boolean hasKey(ItemStack stack, String key) {
		if(!stack.hasTagCompound()) return false;
		return stack.getTagCompound().hasKey(key);
	}
	
	//number formatting utils!
	@SideOnly(Side.CLIENT)
	public static String vagueCrumbCount(int crumbs, ItemStack stack) {
		String itemName = stack.getItem().getItemStackDisplayName(stack).toLowerCase();
		
		//hmm
		if(crumbs < 0) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.hahawhat", itemName).replace('&', '\u00A7');
		
		if(crumbs == 0) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.zero", itemName);
		else if(crumbs == 1) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.one", itemName);
		else if(crumbs <= 8) return I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount.exactplural", crumbs, itemName);
		
		//fight me irl
		String qualifier;
		if (crumbs <= 10) qualifier = "few";
		else if (crumbs <= 25) qualifier = "handful";
		else if (crumbs <= 40) qualifier = "couple";
		else if (crumbs <= 100) qualifier = "fair";
		else if (crumbs <= 150) qualifier = "sizable";
		else if (crumbs <= 300) qualifier = "lot";
		else qualifier = "ton";
		
		String str = I18n.translateToLocalFormatted("breadcrumbtrail.crumbcount." + qualifier, itemName); 
		
		if(BreadcrumbTrail.DEBUGEROO) {
			return TextFormatting.RED + "debug count:" + crumbs;
		} else return str;
	}
	
	//text utils!
	public static String italicise(String in) {
		return TextFormatting.ITALIC + in + TextFormatting.RESET;
	}
	
	//math utils!
	public static int distSquared3d(BlockPos a, BlockPos b) {
		return sq(a.getX() - b.getX()) + sq(a.getY() - b.getY()) + sq(a.getZ() - b.getZ());
	}
	
	public static int sq(int i) {
		return i * i;
	}
}
