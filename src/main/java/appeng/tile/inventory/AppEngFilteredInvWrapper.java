package appeng.tile.inventory;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class AppEngFilteredInvWrapper implements IItemHandlerModifiable 
{
	final private IItemHandlerModifiable handler;
	final private IAEItemFilter filter;
	
	public AppEngFilteredInvWrapper(@Nonnull IItemHandlerModifiable handler, @Nonnull IAEItemFilter filter)
	{
		this.handler = handler;
		this.filter = filter;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		handler.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() 
	{
		return handler.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) 
	{
		return handler.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) 
	{
		if (filter.allowInsert(handler, slot, stack))
			return stack;
		
		return handler.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) 
	{
		if (!filter.allowExtract(handler, slot, amount))
			return ItemStack.EMPTY;

		return handler.extractItem(slot, amount, simulate);
	}

	@Override
	public int getSlotLimit(int slot) 
	{
		return handler.getSlotLimit(slot);
	}
}
