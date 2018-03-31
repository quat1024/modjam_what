package quaternary.breadcrumbtrail;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBreadcrumbPouch extends Item {
	public static final int MAX_CRUMBS = 1024;
	
	public ItemBreadcrumbPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb_pouch"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb_pouch");
		setCreativeTab(BreadcrumbTrail.TAB);
		
		addPropertyOverride(new ResourceLocation(BreadcrumbTrail.MODID, "open"), (stack, world, living) -> isOpen(stack) ? 1 : 0);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack heldStack = player.getHeldItem(hand);
		
		boolean isOpen = isOpen(heldStack);
		setOpen(heldStack, !isOpen);
		return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
	}
	
	//////////////////////////////
	
	@GameRegistry.ItemStackHolder(BreadcrumbTrail.MODID + ":breadcrumb_pouch")
	public static final ItemStack pouchStack = ItemStack.EMPTY;
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == BreadcrumbTrail.TAB) {
			
			for(boolean open : new boolean[]{false, true}) {
				for(int crumbs : new int[]{0, MAX_CRUMBS/2, MAX_CRUMBS}) {
					ItemStack stack = pouchStack.copy();
					setOpen(stack, open);
					setCrumbs(stack, crumbs);
					items.add(stack);
				}
			}
			
		}
	}
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - (getCrumbs(stack) / (double) MAX_CRUMBS);
	}
	
	//well it's the color of bread, so.
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		if(getDurabilityForDisplay(stack) >= 0.9d) return 0xee4422;
		return 0x844f2e;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		String vagueCount = Util.vagueCrumbCount(getCrumbs(stack));
		tooltip.add(vagueCount);
		tooltip.add(I18n.translateToLocal(isOpen(stack) ? "breadcrumbtrail.open" : "breadcrumbtrail.closed"));
		
		super.addInformation(stack, world, tooltip, flag);
	}
	
	////////////////////////////// """api"""
	
	public static final String OPEN_KEY = "OpenBag";
	public static final String CRUMB_KEY = "Crumbs";
	
	public static boolean isOpen(ItemStack stack) {
		return Util.getItemNBTBoolean(stack, "OpenBag", false);
	}
	
	public static void setOpen(ItemStack stack, boolean open) {
		Util.setItemNBTBoolean(stack, OPEN_KEY, open);
	}
	
	public static int getCrumbs(ItemStack stack) {
		return Util.getItemNBTInt(stack, "Crumbs", 0);
	}
	
	public static void setCrumbs(ItemStack stack, int crumbs) {
		Util.setItemNBTInt(stack, CRUMB_KEY, crumbs);
	}
}
