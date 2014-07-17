package com.rae.cnblogs.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

public class CnblogsDataBase extends SQLiteOpenHelper
{
	private final static String	DBName	= "CnBlogs.db";
	private static int			version	= 1;
	private Context				mContext;
	
	public CnblogsDataBase(Context context)
	{
		super(context, DBName, null, version, null);
		this.mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// 初始化表
		db.beginTransaction(); //开始事务
		
		// 读取文件
		try
		{
			InputStream in = mContext.getAssets().open("init.sql");
			BufferedInputStream bi = new BufferedInputStream(in);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = bi.read(buffer)) != -1)
			{
				out.write(buffer, 0, len);
			}
			String sql = out.toString();
			if (TextUtils.isEmpty(sql)) return;
			
			// 根据分号进行分割
			String[] group = sql.split(";");
			for (String strSql : group)
			{
				if (TextUtils.isEmpty(strSql.trim())) continue;
				try
				{
					db.execSQL(strSql);
					Log.i("sql", "执行->" + strSql);
				}
				catch (Exception e)
				{
					Log.e("sql", "数据库初始化失败：" + strSql + ";\r\n" + e.getMessage());
					continue;
				}
			}
			db.setTransactionSuccessful(); // 提交事务
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction(); // 结束事务
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		drop(db, "blogs");
		drop(db, "categroys");
		drop(db, "options");
	}
	
	private void drop(SQLiteDatabase db, String table)
	{
		try
		{
			db.execSQL("drop table " + table);
		}
		catch (Exception e)
		{
			Log.e("sql", "删除数据库出错：" + e.getMessage());
		}
	}
}
