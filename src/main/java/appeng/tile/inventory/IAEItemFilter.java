package appeng.tile.inventory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IAEItemFilter 
{
	boolean allowExtract(IItemHandlerModifiable inv, int slot, int amount );
	boolean allowInsert(IItemHandlerModifiable inv,  int slot, ItemStack stack);
}
