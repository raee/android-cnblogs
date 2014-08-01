package com.rae.cnblogs.sdk.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.R;

/**
 * 数据库帮助类
 * 
 * @author ChenRui
 * 
 */
public class DatabaseOpenHelper extends SQLiteOpenHelper
{
	private Context	mContext;
	
	public DatabaseOpenHelper(Context context)
	{
		super(context, "cnblogs.db", null, 1);
		this.mContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		try
		{
			InputStream inputStream = mContext.getResources().openRawResource(R.raw.init);
			InputStreamReader inReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inReader);
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = reader.readLine()) != null)
			{
				sb.append(str);
			}
			String sql = sb.toString();
			if (TextUtils.isEmpty(sql)) return;
			db.beginTransaction();
			// 根据分号进行分割
			String[] group = sql.split(";");
			for (String strSql : group)
			{
				if (TextUtils.isEmpty(strSql.trim())) continue;
				try
				{
					db.execSQL(strSql);
					Log.i("cnblogs", "执行->" + strSql);
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
