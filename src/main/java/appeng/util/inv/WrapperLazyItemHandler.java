package appeng.util.inv;

import appeng.util.Lazy;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WrapperLazyItemHandler implements IItemHandlerModifiable
{
	private final  Lazy<IItemHandlerModifiable> sourceHandler;
	
	public WrapperLazyItemHandler(Lazy<IItemHandlerModifiable> source)
	{
		this.sourceHandler = source;
	}

	@Override
	public int getSlots() 
	{
		return sourceHandler.get().getSlots();
	}

	@Override
	public ItemStack getStackInSlot( int slot ) 
	{
		return sourceHandler.get().getStackInSlot( slot );
	}

	@Override
	public ItemStack insertItem( int slot, ItemStack stack, boolean simulate ) 
	{
		return sourceHandler.get().insertItem( slot, stack, simulate );		
	}

	@Override
	public ItemStack extractItem( int slot, int amount, boolean simulate ) 
	{
		return sourceHandler.get().extractItem( slot, amount, simulate );
	}

	@Override
	public int getSlotLimit( int slot ) 
	{
		return sourceHandler.get().getSlotLimit( slot );
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) 
	{
		sourceHandler.get().setStackInSlot( slot, stack );
	}
}
