package appeng.tile.inventory;

import appeng.block.AEBaseBlock;
import appeng.tile.AEBaseInvTile;
import net.minecraft.block.Block;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;


public class AppEngInternalSidedInventory extends AppEngInternalInventory implements ISidedInventory 
{
	public AppEngInternalSidedInventory( final AEBaseInvTile inventory, final int size, final int maxStack )
	{
		super(inventory, size, maxStack);
	}

	public AppEngInternalSidedInventory( final AEBaseInvTile inventory, final int size ) 
	{
		super(inventory, size);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) 
	{
		AEBaseInvTile te = (AEBaseInvTile)getTileEntity();
		
		final Block blk = te.getBlockState().getBlock();
		if( blk instanceof AEBaseBlock )
		{
			return te.getAccessibleSlotsBySide( ( (AEBaseBlock) blk ).mapRotation( te, side ) );
		}			
		return te.getAccessibleSlotsBySide( side );	
	}

	@Override
	public boolean canInsertItem(int slotIndex, ItemStack insertingItem, EnumFacing direction) 
	{
		return this.isItemValidForSlot( slotIndex, insertingItem );
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) 
	{
		return true;
	}
}
