package com.rae.cnblogs.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CnblogsDataBase extends SQLiteOpenHelper
{
	private final static String	DBName	= "CnBlogs.db";
	private static int			version	= 1;
	
	public CnblogsDataBase(Context context)
	{
		super(context, DBName, null, version, null);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		// 初始化表
		db.beginTransaction(); //开始事务
		
		db.execSQL("create table()");
		
		db.endTransaction(); // 结束事务
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// 更新数据库，删除数据
		
	}
}
