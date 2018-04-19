package quaternary.breadcrumbtrail.block;

public class BlockBreadcrumb extends BlockBreadcrumbBase {
	public BlockBreadcrumb() {
		setName("breadcrumb");
	}
	
	@Override
	public int getDurabilityBarColor() {
		return 0x844f2e; //dark brownish
	}
}
