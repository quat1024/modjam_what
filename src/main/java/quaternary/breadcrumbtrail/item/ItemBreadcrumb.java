package quaternary.breadcrumbtrail.item;

import net.minecraft.item.ItemBlock;
import quaternary.breadcrumbtrail.Util;
import quaternary.breadcrumbtrail.block.BlockBase;

public class ItemBreadcrumb extends ItemBlock {
	public ItemBreadcrumb(BlockBase crumb) {
		super(crumb);
		Util.setUpItem(this, crumb.getRegistryName());
	}
}
