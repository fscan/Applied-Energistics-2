package appeng.util;

import java.util.function.Supplier;


public class Lazy<T> implements Supplier<T>
{
	final private Supplier<T> supplier;
	private T instance = null;
	
	public Lazy( final Supplier<T> supplier)
	{
		this.supplier = supplier;
	}
	
	@Override
	public T get()
	{
		if ( instance == null )
		{
			instance = supplier.get();
		}
		return instance;
	}
}
