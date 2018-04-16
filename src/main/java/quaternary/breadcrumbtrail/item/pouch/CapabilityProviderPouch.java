package quaternary.breadcrumbtrail.item.pouch;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import quaternary.breadcrumbtrail.util.ItemHandlerHelper2;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityProviderPouch implements ICapabilityProvider {
	//ItemHandlerPouch handler;
	
	ItemStack pouch;
	
	ItemStackHandler handler = new ItemStackHandler(8){
		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stackToInsert, boolean simulate) {
			Item first = findFirstItem();
			
			if(first == Items.AIR || first.equals(stackToInsert.getItem())) return super.insertItem(slot, stackToInsert, simulate);
			else return stackToInsert; //No entry!
		}
		
		@Override
		protected void onContentsChanged(int slot) {
			pouch.setTagInfo("BagContents", serializeNBT());
		}
		
		Item findFirstItem() {
			for(ItemStack s : stacks) {
				if(!s.isEmpty()) return s.getItem();
			}
			return Items.AIR;
		}
	};
	
	public static void readHandlerFromStackNBT(ItemStackHandler handler, ItemStack stack) {
		if(handler == null) return;
		if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("BagContents")) {
			ItemHandlerHelper2.clear(handler); //it must be empty
			return;
		}
		
		handler.deserializeNBT(stack.getTagCompound().getCompoundTag("BagContents"));
	}
	
	public CapabilityProviderPouch(ItemStack pouch) {
		this.pouch = pouch;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}
	
	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			readHandlerFromStackNBT(handler, pouch);
			return (T) handler;
		}
		else return null;
	}
}
