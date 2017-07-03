package appeng.tile.inventory;

import appeng.api.definitions.IItemDefinition;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class AppEngItemDefinitionFilter implements IAEItemFilter 
{
	final private IItemDefinition definition;

	public AppEngItemDefinitionFilter(IItemDefinition definition) 
	{
		this.definition = definition;
	}
	
	@Override
	public boolean allowExtract(IItemHandlerModifiable inv, int slot, int amount) 
	{
		return true;
	}

	@Override
	public boolean allowInsert(IItemHandlerModifiable inv, int slot, ItemStack stack) 
	{
		return definition.isSameAs(stack);
	}

}
