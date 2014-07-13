package com.rae.cnblogs.model;

/**
 * 博客分类
 * 
 * @author Chenrui
 * 
 */
public class BlogCategory
{
	public BlogCategory(String id, String name)
	{
		super();
		this.id = id;
		this.name = name;
	}
	
	private String	id, name;
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setName(String name)
	{
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
}
