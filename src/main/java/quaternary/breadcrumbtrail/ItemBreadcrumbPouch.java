package quaternary.breadcrumbtrail;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class ItemBreadcrumbPouch extends Item {
	
	
	public ItemBreadcrumbPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb_pouch"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb_pouch");
		
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
