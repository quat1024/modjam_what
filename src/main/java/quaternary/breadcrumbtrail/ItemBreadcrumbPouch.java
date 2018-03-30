package quaternary.breadcrumbtrail;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ItemBreadcrumbPouch extends Item {
	public ItemBreadcrumbPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb_pouch"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb_pouch");
	}
}
