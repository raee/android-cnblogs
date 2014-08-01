package com.rae.cnblogs.sdk.model;

/**
 * 博客分类
 * 
 * @author ChenRui
 * 
 */
public class Category
{
	private String	id, name;
	
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
}
