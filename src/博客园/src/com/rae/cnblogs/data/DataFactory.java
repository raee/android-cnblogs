package com.rae.cnblogs.data;

import android.content.Context;

/**
 * 数据工厂
 * 
 * @author Chenrui
 * 
 */
public class DataFactory
{
	private DataFactory()
	{
	}
	
	private static IDataProvider	Provider;
	
	public static IDataProvider getDataProvider(Context context)
	{
		if (Provider == null) Provider = new DataProvider(context.getApplicationContext());
		
		return Provider;
	}
}
