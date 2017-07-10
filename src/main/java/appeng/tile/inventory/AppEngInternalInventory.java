/*
 * This file is part of Applied Energistics 2.
 * Copyright (c) 2013 - 2014, AlgorithmX2, All rights reserved.
 *
 * Applied Energistics 2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Applied Energistics 2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Applied Energistics 2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */

package appeng.tile.inventory;


import java.util.Collections;
import java.util.Iterator;

import javax.annotation.Nonnull;

import appeng.util.Platform;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;


public class AppEngInternalInventory extends ItemStackHandler implements Iterable<ItemStack>
{
	private boolean enableClientEvents = false;
	private IAEAppEngInventory te;
	private int maxStack;
	private InvOperation currentOp;
	private ItemStack previousStack = ItemStack.EMPTY;
	private IAEItemFilter filter;
	
	public AppEngInternalInventory( final IAEAppEngInventory inventory, final int size, final int maxStack, IAEItemFilter filter )
	{
		super(size);
		this.setTileEntity( inventory );
		this.setFilter(filter);
		this.maxStack = maxStack;
	}

	public AppEngInternalInventory( final IAEAppEngInventory inventory, final int size, final int maxStack)
	{
		this(inventory, size, maxStack, null);
	}
	
	public AppEngInternalInventory( final IAEAppEngInventory inventory, final int size)
	{
		this(inventory, size, 64);
	}
	
	public void setFilter(IAEItemFilter filter)
	{
		this.filter = filter;
	}
	
	@Override
	public int getSlotLimit(int slot)
	{
		return maxStack;
	}
	
	@Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack)
    {				
		currentOp = InvOperation.setStack;
		previousStack = getStackInSlot(slot);
		super.setStackInSlot(slot, stack);
    }
	
	@Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate)
    {
		if (filter != null && !filter.allowInsert(this, slot, stack))
			return stack;
			
		currentOp = InvOperation.insert;
		previousStack = getStackInSlot(slot).copy();
		return super.insertItem(slot, stack, simulate);		
    }
	
	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		if (filter != null && !filter.allowExtract(this, slot, amount))
			return ItemStack.EMPTY;
		
		currentOp = InvOperation.extract;
		previousStack = getStackInSlot(slot);
		return super.extractItem(slot, amount, simulate);				
	}
	
	@Override
    protected void onContentsChanged(int slot)
    {
		if( this.getTileEntity() != null && this.eventsEnabled() )
		{
			ItemStack added = getStackInSlot(slot).copy();
			ItemStack removed = previousStack.copy();
			
			if (currentOp == InvOperation.insert)
			{
				added.grow( -removed.getCount() );
			}
			else if (currentOp == InvOperation.extract)
			{
				removed.grow( -added.getCount() );
			}
			this.getTileEntity().onChangeInventory( this, slot, currentOp, removed, added );								
		}
		super.onContentsChanged( slot );
    }
	
	protected boolean eventsEnabled()
	{
		return Platform.isServer() || this.isEnableClientEvents();
	}

	public void setMaxStackSize( final int s )
	{
		this.maxStack = s;
	}


	public void writeToNBT( final NBTTagCompound data, final String name )
	{
		data.setTag( name, serializeNBT() );
	}

	public void readFromNBT( final NBTTagCompound data, final String name )
	{
		final NBTTagCompound c = data.getCompoundTag( name );
		if( c != null )
		{
			readFromNBT( c );
		}
	}
	
	public void readFromNBT( final NBTTagCompound data )
	{
		deserializeNBT( data);
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		return Collections.unmodifiableList(super.stacks).iterator();
	}

	private boolean isEnableClientEvents()
	{
		return this.enableClientEvents;
	}

	public void setEnableClientEvents( final boolean enableClientEvents )
	{
		this.enableClientEvents = enableClientEvents;
	}

	private IAEAppEngInventory getTileEntity()
	{
		return this.te;
	}

	public void setTileEntity( final IAEAppEngInventory te )
	{
		this.te = te;
	}
}
