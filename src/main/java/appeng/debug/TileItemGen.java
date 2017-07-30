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

package appeng.debug;


import java.util.LinkedList;
import java.util.Queue;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import appeng.tile.AEBaseInvTile;
import appeng.util.inv.InvOperation;


public class TileItemGen extends AEBaseInvTile
{
	private static final Queue<ItemStack> POSSIBLE_ITEMS = new LinkedList<>();
	private IItemHandler itemGen = new ItemGenHandler();

	public TileItemGen()
	{
		if( POSSIBLE_ITEMS.isEmpty() )
		{
			for( final Object obj : Item.REGISTRY )
			{
				final Item mi = (Item) obj;
				if( mi != null )
				{
					if( mi.isDamageable() )
					{
						for( int dmg = 0; dmg < mi.getMaxDamage(); dmg++ )
						{
							POSSIBLE_ITEMS.add( new ItemStack( mi, 1, dmg ) );
						}
					}
					else
					{
						final NonNullList<ItemStack> list = NonNullList.create();
						if( mi.getCreativeTab() != null )
						{
							mi.getSubItems( mi.getCreativeTab(), list );
							POSSIBLE_ITEMS.addAll( list );
						}
					}
				}
			}
		}
	}

	private class ItemGenHandler implements IItemHandler
	{

		@Override
		public int getSlots()
		{
			return 1;
		}

		@Override
		public ItemStack getStackInSlot( int slot )
		{
			return POSSIBLE_ITEMS.peek();
		}

		@Override
		public ItemStack insertItem( int slot, ItemStack stack, boolean simulate )
		{
			return stack;
		}

		@Override
		public ItemStack extractItem( int slot, int amount, boolean simulate )
		{
			if( simulate )
			{
				return ItemHandlerHelper.copyStackWithSize( POSSIBLE_ITEMS.peek(), amount );
			}
			else
			{
				final ItemStack a = POSSIBLE_ITEMS.poll();
				POSSIBLE_ITEMS.add( a );
				return ItemHandlerHelper.copyStackWithSize( a, amount );
			}
		}

		@Override
		public int getSlotLimit( int slot )
		{
			return 1;
		}
	}

	@Override
	public IItemHandler getInternalInventory()
	{
		return itemGen;
	}

	@Override
	public void onChangeInventory( IItemHandler inv, int slot, InvOperation mc, ItemStack removed, ItemStack added )
	{
		// NOP
	}
}
