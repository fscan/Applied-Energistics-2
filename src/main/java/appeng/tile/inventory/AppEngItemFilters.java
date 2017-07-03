package appeng.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class AppEngItemFilters 
{
	public static final IAEItemFilter INSERT_ONLY = new InsertOnlyFilter();
	public static final IAEItemFilter EXTRACT_ONLY = new ExtractOnlyFilter();
	
	
	private static class InsertOnlyFilter implements IAEItemFilter
	{
		@Override
		public boolean allowExtract(IItemHandlerModifiable inv, int slot, int amount) 
		{
			return false;
		}

		@Override
		public boolean allowInsert(IItemHandlerModifiable inv, int slot, ItemStack stack) 
		{
			return true;
		}		
	}
	
	private static class ExtractOnlyFilter implements IAEItemFilter
	{
		@Override
		public boolean allowExtract(IItemHandlerModifiable inv, int slot, int amount) 
		{
			return true;
		}

		@Override
		public boolean allowInsert(IItemHandlerModifiable inv, int slot, ItemStack stack) 
		{
			return false;
		}		
	}
}
