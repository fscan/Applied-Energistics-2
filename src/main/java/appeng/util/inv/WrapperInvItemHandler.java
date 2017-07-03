package appeng.util.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WrapperInvItemHandler implements IInventory 
{
	final private IItemHandlerModifiable inv;
	
	public WrapperInvItemHandler(final IItemHandlerModifiable inv) 
	{
		this.inv = inv;
	}
	
	@Override
	public String getName() 
	{
		return null;
	}

	@Override
	public boolean hasCustomName() 
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName() 
	{
		return null;
	}

	@Override
	public int getSizeInventory() 
	{
		return inv.getSlots();
	}

	@Override
	public boolean isEmpty() 
	{
		for( int x = 0; x < inv.getSlots(); x++ )
		{
			if( !inv.getStackInSlot( x ).isEmpty() )
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) 
	{
		return inv.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) 
	{
		return inv.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) 
	{
		return inv.extractItem(index, inv.getSlotLimit(index), false);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) 
	{
		inv.setStackInSlot(index, stack);
	}

	@Override
	public int getInventoryStackLimit() 
	{
		int max = 0;
		for (int i = 0; i < inv.getSlots();  ++i)
		{
			max = Math.max( max, inv.getSlotLimit( i ) );
		}
		return max;
	}

	@Override
	public void markDirty() 
	{
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) 
	{
		return false;
	}

	@Override
	public void openInventory(EntityPlayer player) 
	{
	}

	@Override
	public void closeInventory(EntityPlayer player) 
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) 
	{
		//copied from SlotItemHandler
        if (stack.isEmpty())
            return false;
        
		ItemStack currentStack = inv.getStackInSlot(index);

		inv.setStackInSlot(index, ItemStack.EMPTY);
		ItemStack remainder = inv.insertItem(index, stack, true);
        inv.setStackInSlot(index, currentStack);
          
        return remainder.isEmpty() || remainder.getCount() < stack.getCount();
	}

	@Override
	public int getField(int id) 
	{
		return 0;
	}

	@Override
	public void setField(int id, int value) 
	{
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() 
	{
		for( int x = 0; x < inv.getSlots(); x++ )
		{
			inv.setStackInSlot( x, ItemStack.EMPTY );
		}
	}

}
