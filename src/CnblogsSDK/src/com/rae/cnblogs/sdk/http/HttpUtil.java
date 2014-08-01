package com.rae.cnblogs.sdk.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Http帮助类
 * 
 * @author ChenRui
 * 
 */
public final class HttpUtil
{
	/**
	 * 检查可用网络是否连接成功
	 * 
	 * @param context
	 * @return
	 */
	public static boolean IsConnected(Context context)
	{
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfos = conn.getAllNetworkInfo();
		
		for (NetworkInfo networkInfo : netInfos)
		{
			if (networkInfo.isAvailable() && networkInfo.isConnected())
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断是否处于Wifi网络
	 * 
	 * @param context
	 * @return -1：网络没有打开；0：其他类型的网络（移动网络）；1：WIFI网络
	 * 
	 */
	public static int IsWifi(Context context)
	{
		ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if (info == null || !info.isAvailable() || !info.isConnectedOrConnecting())
		{
			return -1; //网络不可用，没有打开，或者上不了网
		}
		if (info.getType() == ConnectivityManager.TYPE_WIFI)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
}
