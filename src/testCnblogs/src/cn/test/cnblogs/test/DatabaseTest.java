package cn.test.cnblogs.test;

import java.util.List;

import com.rae.cnblogs.sdk.data.DataProvider;
import com.rae.cnblogs.sdk.data.DatabaseOpenHelper;
import com.rae.cnblogs.sdk.data.IDataProvider;
import com.rae.cnblogs.sdk.model.Blog;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

public class DatabaseTest extends AndroidTestCase
{
	public void testCopy()
	{
		DatabaseOpenHelper open = new DatabaseOpenHelper(mContext);
		SQLiteDatabase db = open.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from categroys", null);
		while (cursor.moveToNext())
		{
			show(cursor.getString(cursor.getColumnIndex("cate_name")));
		}
		cursor.close();
		show(db.getPath());
		show(db.isOpen());
	}
	
	public void testGetBlog()
	{
		IDataProvider db = new DataProvider(mContext);
		List<Blog> blogs = db.GetBlogs(1);
		for (Blog blog : blogs)
		{
			show("数据库博客：" + blog.getTitle());
		}
	}
	
	void show(Object msg)
	{
		Log.i("cnblogs", msg.toString());
	}
}
