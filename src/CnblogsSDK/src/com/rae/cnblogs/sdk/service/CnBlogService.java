package com.rae.cnblogs.sdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CnBlogService extends Service
{
	private CnBlogServiceBinder	mBinder;
	
	@Override
	public IBinder onBind(Intent intent)
	{
		if (mBinder == null)
		{
			mBinder = new CnBlogServiceBinder();
		}
		
		return mBinder;
	}
	
	@Override
	public void onCreate()
	{
	}
	
}
