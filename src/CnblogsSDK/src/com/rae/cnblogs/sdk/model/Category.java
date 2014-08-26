package com.rae.cnblogs.sdk.model;

import android.database.Cursor;

/**
 * 博客分类
 * 
 * @author ChenRui
 * 
 */
public class Category
{
	private String	id, name;
	private boolean	isShow;
	
	public Category()
	{
	}
	
	public Category(String id, String name)
	{
		this.id = id;
		this.name = name;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Category toCategory(Cursor cursor)
	{
		this.id = cursor.getString(cursor.getColumnIndex("cateid"));
		this.name = cursor.getString(cursor.getColumnIndex("cate_name"));
		this.isShow = cursor.getInt(cursor.getColumnIndex("is_show")) == 1;
		
		return this;
	}
	
	public boolean isShow()
	{
		return isShow;
	}
	
	public void setShow(boolean isShow)
	{
		this.isShow = isShow;
	}
}
