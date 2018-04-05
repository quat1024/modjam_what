package quaternary.breadcrumbtrail.item;

import net.minecraft.item.ItemBlock;
import quaternary.breadcrumbtrail.block.BlockBreadcrumb;

public class ItemBlockBreadcrumb extends ItemBlock {
	public ItemBlockBreadcrumb(BlockBreadcrumb block) {
		super(block);
		
		//Pls
		setRegistryName(block.getRegistryName());
	}
}
