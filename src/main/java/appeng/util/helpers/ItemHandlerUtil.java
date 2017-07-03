package appeng.util.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemHandlerUtil 
{
	public static void clear(IItemHandlerModifiable inv)
	{
		for( int x = 0; x < inv.getSlots(); x++ )
		{
			inv.setStackInSlot( x, ItemStack.EMPTY );
		}
	}
	
	public static boolean isEmpty(IItemHandler inv)
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
