package com.rae.cnblogs.sdk.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Category;

public class DataProvider implements IDataProvider
{
	private SQLiteDatabase	db;
	
	public DataProvider(Context context)
	{
		DatabaseOpenHelper dbHelper = new DatabaseOpenHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	@Override
	public List<Blog> GetBlogs(int page)
	{
		return GetBlogs("", page);
	}
	
	@Override
	public List<Blog> GetBlogs(String cateId, int page)
	{
		if ((page - 1) < 0) page = 1;
		page = (page - 1) * 20; //20分页
		List<Blog> result = new ArrayList<Blog>();
		Cursor cursor = db.rawQuery("select * from blogs where cateid like '%" + cateId + "%' limit " + page + ",20 ", null);
		cursor.moveToFirst();
		while (cursor.moveToNext())
		{
			result.add(new Blog().toBlog(cursor));
		}
		
		return result;
	}
	
	@Override
	public boolean existBlog(String id)
	{
		Cursor cursor = db.rawQuery("select count(*) from blogs where blogid=?", new String[] { id });
		if (cursor.moveToFirst())
		{
			return cursor.getInt(0) > 0;
		}
		return false;
	}
	
	@Override
	public void addOrUpdateBlogs(List<Blog> blogs)
	{
		try
		{
			db.beginTransaction();
			for (Blog blog : blogs)
			{
				if (existBlog(blog.getId()))
				{
					updateBlog(blog);
					continue;
				}
				//新增
				addBlog(blog);
			}
			db.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction();
		}
	}
	
	public void addBlog(Blog blog)
	{
		// 判断是新增还是更新
		if (existBlog(blog.getId()))
		{
			return;
		}
		Log.i("cnblogs", "插入博客：" + blog.getTitle());
		String[] param = new String[] { blog.getId(), blog.getCateId(), blog.getTitle(), blog.getSummary(), "", blog.getAutor(), blog.getViewCount(), blog.getCommentCount(),
				blog.getPostDate() };
		db.execSQL("insert into blogs(blogid,cateid,title,summary,body,author,viewcout,commentcount,postdate)values(?,?,?,?,?,?,?,?,?)", param);
	}
	
	public void updateBlog(Blog blog)
	{
		Log.i("cnblogs", "更新博客：" + blog.getTitle());
		String[] param = new String[] { blog.getCateId(), blog.getTitle(), blog.getSummary(), blog.getContent(), blog.getAutor(), blog.getViewCount(), blog.getCommentCount(),
				blog.getPostDate(), blog.getId() };
		db.execSQL("update blogs set cateid=?,title=?,summary=?,body=?,author=?,viewcout=?,commentcount=?,postdate=? where blogid=?", param);
	}
	
	@Override
	public List<Category> getCategories()
	{
		List<Category> result = new ArrayList<Category>();
		Cursor cursor = db.rawQuery("select * from categroys where is_show=1 order by cate_order", null);
		cursor.moveToFirst();
		while (cursor.moveToNext())
		{
			result.add(new Category().toCategory(cursor));
		}
		return result;
	}
}
