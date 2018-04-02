package quaternary.breadcrumbtrail;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBreadcrumbPouch extends Item {
	public static final int MAX_CRUMBS = 512;
	
	public ItemBreadcrumbPouch() {
		setRegistryName(new ResourceLocation(BreadcrumbTrail.MODID, "breadcrumb_pouch"));
		setUnlocalizedName(BreadcrumbTrail.MODID + ".breadcrumb_pouch");
		setCreativeTab(BreadcrumbTrail.TAB);
		setMaxStackSize(1);
		
		addPropertyOverride(new ResourceLocation(BreadcrumbTrail.MODID, "open"), (stack, world, living) -> isOpen(stack) ? 1 : 0);
	}
	
	////////////////////////////// open and close the bag
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack heldStack = player.getHeldItem(hand);
		
		boolean isOpen = isOpen(heldStack);
		setOpen(heldStack, !isOpen);
		//make sure it doesnt instantly place a crumb
		setLastPosition(heldStack, player.getPosition());
		
		return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
	}
	
	@Override
	public void onCreated(ItemStack stack, World worldIn, EntityPlayer player) {
		setLastPosition(stack, player.getPosition());
	}
	
	////////////////////////////// leak breadcrumbs all over the floor
	
	@GameRegistry.ObjectHolder(BreadcrumbTrail.MODID + ":breadcrumb")
	public static final Block BREADCRUMB_BLOCK = Blocks.DIAMOND_BLOCK;
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(world.isRemote || !isOpen(stack) || getCrumbs(stack) == 0 || !(entity instanceof EntityPlayer)) return;
		
		BlockPos entPos = entity.getPosition();
		BlockPos lastCrumbPos = getLastPosition(stack);
		
		int dx = entPos.getX() - lastCrumbPos.getX();
		int dz = entPos.getZ() - lastCrumbPos.getZ();
		dx *= dx; dz *= dz;
		int distSquared = dx + dz;
		
		if(distSquared >= 9 * 9) {
			//is it ok to place one here?
			if(world.getBlockState(entPos).getBlock() instanceof BlockBreadcrumb) return;
			if(!world.getBlockState(entPos).getBlock().isReplaceable(world, entPos)) return;
			if(!BREADCRUMB_BLOCK.canPlaceBlockAt(world, entPos)) return;
			
			//place a crumb
			setCrumbs(stack, getCrumbs(stack) - 1);
			setLastPosition(stack, entPos);
			world.setBlockState(entPos, BREADCRUMB_BLOCK.getDefaultState(), 3);
			world.playSound(null, entPos, SoundEvents.BLOCK_STONE_STEP, SoundCategory.BLOCKS, 0.7f, 2f);
			
			((EntityPlayer) entity).addStat(BreadcrumbTrail.LEAVE_BREADCRUMB_STAT, 1);
		}
	}
	
	
	////////////////////////////// display
	
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
		
		if(getCrumbs(stack) == 0) {
			tooltip.add("");
			
			String useBtnString = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getDisplayName();
			
			tooltip.add(Util.italicise(I18n.translateToLocalFormatted("breadcrumbtrail.breadcrumb_pouch.hint1", useBtnString)));
			tooltip.add(Util.italicise(I18n.translateToLocal("breadcrumbtrail.breadcrumb_pouch.hint2")));
		}
		
		super.addInformation(stack, world, tooltip, flag);
	}
	
	////////////////////////////// """api"""
	
	public static final String OPEN_KEY = "OpenBag";
	public static final String CRUMB_KEY = "Crumbs";
	public static final String POS_KEY = "LastCrumbPosition";
	
	public static boolean isOpen(ItemStack stack) {
		return Util.getItemNBTBoolean(stack, OPEN_KEY, false);
	}
	
	public static void setOpen(ItemStack stack, boolean open) {
		Util.setItemNBTBoolean(stack, OPEN_KEY, open);
	}
	
	public static int getCrumbs(ItemStack stack) {
		return Util.getItemNBTInt(stack, CRUMB_KEY, 0);
	}
	
	public static void setCrumbs(ItemStack stack, int crumbs) {
		Util.setItemNBTInt(stack, CRUMB_KEY, crumbs);
	}
	
	public static BlockPos getLastPosition(ItemStack stack) {
		return Util.getItemNBTBlockPos(stack, POS_KEY, new BlockPos(0,0,0));
	}
	
	public static void setLastPosition(ItemStack stack, BlockPos pos) {
		Util.setItemNBTBlockPos(stack, POS_KEY, pos);
	}
}
