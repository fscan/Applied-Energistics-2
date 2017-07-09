package appeng.util.inv;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class WrapperChainedItemHandler implements IItemHandler 
{
	private IItemHandler[] itemHandler; // the handlers	
	private int[] baseIndex; // index-offsets of the different handlers
	private int slotCount; // number of total slots

	public WrapperChainedItemHandler(IItemHandler... itemHandler)
	{
		setItemHandlers(itemHandler);
	}

	private void setItemHandlers(IItemHandler []handlers)
	{
		this.itemHandler = handlers;
	    this.baseIndex = new int[itemHandler.length];
	    int index = 0;
	    for (int i = 0; i < itemHandler.length; i++)
	    {
	    	index += itemHandler[i].getSlots();
	        baseIndex[i] = index;
	    }
	    this.slotCount = index;		
	}

	
	// returns the handler index for the slot
	private int getIndexForSlot(int slot)
	{
		if (slot < 0)
			return -1;

        for (int i = 0; i < baseIndex.length; i++)
        {
            if (slot - baseIndex[i] < 0)
            {
                return i;
            }
        }
        return -1;
    }

    private IItemHandler getHandlerFromIndex(int index)
    {
        if (index < 0 || index >= itemHandler.length)
        {
            return EmptyHandler.INSTANCE;
        }
        return itemHandler[index];
    }

    private int getSlotFromIndex(int slot, int index)
    {
        if (index <= 0 || index >= baseIndex.length)
        {
            return slot;
        }
        return slot - baseIndex[index - 1];
    }
    
    public void cycleOrder()
    {
    	if ( this.itemHandler.length > 1 )
    	{
    		ArrayList<IItemHandler> newOrder = new  ArrayList<>();
    		newOrder.add( this.itemHandler[ this.itemHandler.length - 1 ] );
    		for ( int i = 0; i < this.itemHandler.length - 1; ++i )
    		{
    			newOrder.add( this.itemHandler[i] );
    		}
    		this.setItemHandlers( newOrder.toArray( new IItemHandler[ this.itemHandler.length ] ));
    	}    	
    }
    

    @Override
    public int getSlots()
    {
        return slotCount;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot)
    {
        int index = getIndexForSlot(slot);
        IItemHandler handler = getHandlerFromIndex(index);
        slot = getSlotFromIndex(slot, index);
        return handler.getStackInSlot(slot);
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
        int index = getIndexForSlot(slot);
        IItemHandler handler = getHandlerFromIndex(index);
        slot = getSlotFromIndex(slot, index);
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate)
    {
        int index = getIndexForSlot(slot);
        IItemHandler handler = getHandlerFromIndex(index);
        slot = getSlotFromIndex(slot, index);
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        int index = getIndexForSlot(slot);
        IItemHandler handler = getHandlerFromIndex(index);
        int localSlot = getSlotFromIndex(slot, index);
        return handler.getSlotLimit(localSlot);
    }
}
