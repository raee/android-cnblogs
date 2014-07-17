package com.rae.cnblogs.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.rae.cnblogs.model.Blog;
import com.rae.cnblogs.model.BlogCategory;

public class DataProvider implements IDataProvider
{
	
	private CnblogsDataBase	database;
	
	public DataProvider(Context context)
	{
		this.database = new CnblogsDataBase(context);
	}
	
	@Override
	public boolean exitsBlog(String blogId)
	{
		boolean result = false;
		SQLiteDatabase db = database.getReadableDatabase();
		Cursor cursor = db.rawQuery("select blogid from blogs where blogid=?", new String[] { blogId });
		if (cursor != null) result = cursor.getCount() > 0;
		return result;
		
	}
	
	public void addBlog(Blog model)
	{
		if (model == null || TextUtils.isEmpty(model.getId()) || exitsBlog(model.getId())) return;
		SQLiteDatabase db = database.getWritableDatabase();
		ContentValues values = new ContentValues();
		
		values.put("blogid", model.getId());
		values.put("cateid", model.getCateId());
		values.put("title", model.getTitle());
		values.put("summary", model.getSummary());
		values.put("body", model.getContent());
		values.put("author", model.getAutor());
		values.put("body", model.getContent() == null ? "" : model.getContent());
		values.put("viewcout", model.getViewCount());
		values.put("commentcount", model.getCommentCount());
		values.put("postdate", model.getPostDate());
		db.insert("blogs", "", values);
		
	}
	
	@Override
	public void updateBlog(Blog model)
	{
		
	}
	
	@Override
	public List<Blog> getBlogList(int page)
	{
		List<Blog> result = new ArrayList<Blog>();
		SQLiteDatabase db = database.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from blogs order by blogid limit ?,20", new String[] { page + "" });
		cursor.moveToFirst();
		while (cursor.moveToNext())
		{
			result.add(toBlog(cursor));
		}
		return result;
	}
	
	private Blog toBlog(Cursor cursor)
	{
		Blog m = new Blog();
		m.setId(cursor.getString(cursor.getColumnIndex("blogid")));
		m.setCateId(cursor.getString(cursor.getColumnIndex("title")));
		m.setTitle(cursor.getString(cursor.getColumnIndex("title")));
		m.setSummary(cursor.getString(cursor.getColumnIndex("summary")));
		m.setContent(cursor.getString(cursor.getColumnIndex("body")));
		m.setAutor(cursor.getString(cursor.getColumnIndex("author")));
		m.setViewCount(cursor.getInt(cursor.getColumnIndex("viewcout")));
		m.setCommentCount(cursor.getInt(cursor.getColumnIndex("commentcount")));
		m.setPostDate(cursor.getString(cursor.getColumnIndex("postdate")));
		return m;
	}
	
	@Override
	public List<BlogCategory> getCategroy(int status)
	{
		return null;
	}
	
	@Override
	public void updateCategroy(String cateId, int status, int order)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void addBlogs(List<Blog> model)
	{
		for (Blog blog : model)
		{
			addBlog(blog);
		}
	}
	
}
