package quaternary.breadcrumbtrail;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import quaternary.breadcrumbtrail.item.pouch.ItemBreadcrumbPouch;

public class BreadcrumbCreative extends CreativeTabs {
	@GameRegistry.ItemStackHolder(
		value = BreadcrumbTrail.MODID + ":breadcrumb_pouch",
		nbt = "{" + ItemBreadcrumbPouch.HIDE_BAR_KEY + ":1b}"
	)
	public static final ItemStack tabStack = ItemStack.EMPTY;
	
	public BreadcrumbCreative() {
		super(BreadcrumbTrail.MODID);
	}
	
	@Override
	public ItemStack getTabIconItem() {
		return tabStack;
	}
}
