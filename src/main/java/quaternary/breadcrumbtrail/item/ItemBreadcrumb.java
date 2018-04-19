package quaternary.breadcrumbtrail.item;

import net.minecraft.item.ItemBlock;
import quaternary.breadcrumbtrail.block.BlockBreadcrumbBase;
import quaternary.breadcrumbtrail.util.Util;

public class ItemBreadcrumb extends ItemBlock {
	BlockBreadcrumbBase crumb;
	
	public ItemBreadcrumb(BlockBreadcrumbBase crumb) {
		super(crumb);
		this.crumb = crumb;
		Util.setUpItem(this, crumb.getRegistryName());
	}
	
	public int getDurabilityBarColor() {
		return crumb.getDurabilityBarColor();
	}
}
