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

package appeng.tile;


import java.util.EnumMap;

import javax.annotation.Nullable;

import appeng.tile.events.TileEventType;
import appeng.tile.inventory.IAEAppEngInventory;
import appeng.tile.inventory.InvOperation;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;


public abstract class AEBaseInvTile extends AEBaseTile implements IAEAppEngInventory
{

	private EnumMap<EnumFacing, IItemHandler> sidedItemHandler = new EnumMap<>( EnumFacing.class );

	private IItemHandler itemHandler;

	@TileEvent( TileEventType.WORLD_NBT_READ )
	public void readFromNBT_AEBaseInvTile( final net.minecraft.nbt.NBTTagCompound data )
	{
		final IInventory inv = this.getInternalInventory();
		final NBTTagCompound opt = data.getCompoundTag( "inv" );
		for( int x = 0; x < inv.getSizeInventory(); x++ )
		{
			final NBTTagCompound item = opt.getCompoundTag( "item" + x );
			inv.setInventorySlotContents( x, new ItemStack( item ) );
		}
	}

	public abstract IInventory getInternalInventory();

	@TileEvent( TileEventType.WORLD_NBT_WRITE )
	public void writeToNBT_AEBaseInvTile( final net.minecraft.nbt.NBTTagCompound data )
	{
		final IInventory inv = this.getInternalInventory();
		final NBTTagCompound opt = new NBTTagCompound();
		for( int x = 0; x < inv.getSizeInventory(); x++ )
		{
			final NBTTagCompound item = new NBTTagCompound();
			final ItemStack is = inv.getStackInSlot( x );
			if( !is.isEmpty() )
			{
				is.writeToNBT( item );
			}
			opt.setTag( "item" + x, item );
		}
		data.setTag( "inv", opt );
	}


	/**
	 * Returns if the inventory is named
	 */
	@Override
	public boolean hasCustomName()
	{
		return this.getInternalInventory().hasCustomName();
	}

	@Override
	public abstract void onChangeInventory( IInventory inv, int slot, InvOperation mc, ItemStack removed, ItemStack added );

	@Override
	public ITextComponent getDisplayName()
	{
		if( this.hasCustomName() )
		{
			return new TextComponentString( this.getCustomName() );
		}
		return new TextComponentTranslation( this.getBlockType().getUnlocalizedName() );
	}

	

	@Override
	public boolean hasCapability( Capability<?> capability, EnumFacing facing )
	{
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability( capability, facing );
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public <T> T getCapability( Capability<T> capability, @Nullable EnumFacing facing )
	{
		if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY )
		{
			IInventory inv = getInternalInventory();
			
			if( facing == null || !(inv instanceof ISidedInventory))
			{
				if( itemHandler == null )
				{
					itemHandler = new InvWrapper( inv );
				}
				return (T) itemHandler;
			}
			else
			{
				return (T) sidedItemHandler.computeIfAbsent( facing, side -> new SidedInvWrapper( (ISidedInventory) inv , side ) );
			}
		}
		return super.getCapability( capability, facing );
	}

	
	public abstract int[] getAccessibleSlotsBySide( EnumFacing whichSide );
}
