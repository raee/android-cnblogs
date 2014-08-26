package com.rae.cnblogs;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public final class AndroidUtils
{
	/**
	 * 获取屏幕信息
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrices(Context context)
	{
		WindowManager windowManger = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		windowManger.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics;
	}
	
	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayWidth(Context context)
	{
		return getDisplayMetrices(context).widthPixels;
	}
	
	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getDisplayHeight(Context context)
	{
		return getDisplayMetrices(context).heightPixels;
	}
}
