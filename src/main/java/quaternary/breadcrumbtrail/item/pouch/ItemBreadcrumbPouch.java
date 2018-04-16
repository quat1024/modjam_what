package quaternary.breadcrumbtrail.item.pouch;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.*;
import quaternary.breadcrumbtrail.*;
import quaternary.breadcrumbtrail.Util;
import quaternary.breadcrumbtrail.block.BlockBreadcrumb;
import quaternary.breadcrumbtrail.block.BlockBreadcrumbBase;
import quaternary.breadcrumbtrail.util.ItemHandlerHelper2;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBreadcrumbPouch extends Item {
	public static final int MAX_CRUMBS = 512;
	
	public ItemBreadcrumbPouch() {
		Util.setUpItem(this, "breadcrumb_pouch");
		
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
	
	////////////////////////////// manipulate items in the bag
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		return new CapabilityProviderPouch(stack);
	}
	
	
	////////////////////////////// leak breadcrumbs all over the floor
	
	@GameRegistry.ObjectHolder(BreadcrumbTrail.MODID + ":breadcrumb")
	public static final Block BREADCRUMB_BLOCK = Blocks.DIAMOND_BLOCK;
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(world.isRemote || !isOpen(stack) || !(entity instanceof EntityPlayer)) return;
		
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if(ItemHandlerHelper2.isEmpty(handler)) return;
		
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
			Item crumb = handler.extractItem(0, 1, false).getItem();
			
			setLastPosition(stack, entPos);
			world.setBlockState(entPos, Block.getBlockFromItem(crumb).getDefaultState(), 3);
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
				for(int crumbCount : new int[]{0, MAX_CRUMBS/2, MAX_CRUMBS}) {
					for(BlockBreadcrumbBase crumb : BreadcrumbTrail.CRUMBS) {
						ItemStack stack = pouchStack.copy();
						setOpen(stack, open);
						fillBagWith(stack, new ItemStack(Item.getItemFromBlock(crumb), crumbCount));
						items.add(stack);
					}
				}
			}
			
		}
	}
	
	//used so there's no ugly bar in creative mode
	public static final String HIDE_BAR_KEY = "HideBar";
	
	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return !Util.getItemNBTBoolean(stack, HIDE_BAR_KEY, false);
	}
	
	private double getFillPercentage(ItemStack stack) {
		return countCrumbs(stack) / (double) MAX_CRUMBS;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - getFillPercentage(stack);
	}
	
	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		if(getFillPercentage(stack) < 0.1) return 0xee4422;
		return 0x844f2e;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {		
		int count = countCrumbs(stack);
		
		String vagueCount = Util.vagueCrumbCount(count);
		tooltip.add(vagueCount);
		tooltip.add(I18n.translateToLocal(isOpen(stack) ? "breadcrumbtrail.open" : "breadcrumbtrail.closed"));
		
		if(count == 0) {
			tooltip.add("");
			
			String useBtnString = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getDisplayName();
			
			tooltip.add(Util.italicise(I18n.translateToLocalFormatted("breadcrumbtrail.breadcrumb_pouch.hint1", useBtnString)));
			tooltip.add(Util.italicise(I18n.translateToLocal("breadcrumbtrail.breadcrumb_pouch.hint2")));
		}
		
		super.addInformation(stack, world, tooltip, flag);
	}
	
	////////////////////////////// """api"""
	
	public static final String OPEN_KEY = "OpenBag";
	public static final String POS_KEY = "LastCrumbPosition";
	
	public static boolean isOpen(ItemStack stack) {
		return Util.getItemNBTBoolean(stack, OPEN_KEY, false);
	}
	
	public static void setOpen(ItemStack stack, boolean open) {
		Util.setItemNBTBoolean(stack, OPEN_KEY, open);
	}
	
	public static int countCrumbs(ItemStack stack) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		return handler == null ? 0 : ItemHandlerHelper2.countItems(handler);
	}
	
	public static void fillBagWith(ItemStack stack, ItemStack crumbs) {
		IItemHandler handler = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		
		ItemHandlerHelper2.clear(handler);
		ItemHandlerHelper.insertItem(handler, crumbs, false);
	}
	
	public static BlockPos getLastPosition(ItemStack stack) {
		return Util.getItemNBTBlockPos(stack, POS_KEY, new BlockPos(0,0,0));
	}
	
	public static void setLastPosition(ItemStack stack, BlockPos pos) {
		Util.setItemNBTBlockPos(stack, POS_KEY, pos);
	}
}
