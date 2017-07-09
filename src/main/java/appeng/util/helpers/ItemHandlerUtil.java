package appeng.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemHandlerUtil 
{
	public static void clear( final IItemHandler inv )
	{
		for( int x = 0; x < inv.getSlots(); x++ )
		{
			setStackInSlot( inv, x, ItemStack.EMPTY );
		}
	}
	
	public static ItemStack setStackInSlot( final IItemHandler inv, final int slot, final ItemStack stack )
	{
		if ( inv instanceof IItemHandlerModifiable )
		{
			( (IItemHandlerModifiable) inv ).setStackInSlot( slot, stack );
			return stack;
		}
		else
		{
			final ItemStack old = inv.getStackInSlot( slot );
			if ( old.isItemEqual( stack ) )
			{
				if ( old.getCount() > stack.getCount() )
				{
					inv.extractItem( slot, old.getCount() - stack.getCount(), false);
				}
				else if ( old.getCount() < stack.getCount() )
				{
					inv.insertItem(slot, ItemHandlerHelper.copyStackWithSize( stack, stack.getCount() - old.getCount() ), false);
				}
			}
			else
			{
				if ( !old.isEmpty() )
				{
					inv.extractItem( slot, old.getCount(), false );
				}
				inv.insertItem( slot, stack, false);
			}
			return inv.getStackInSlot( slot );
		}
	}
	
	public static boolean isEmpty( final IItemHandler inv )
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
}
