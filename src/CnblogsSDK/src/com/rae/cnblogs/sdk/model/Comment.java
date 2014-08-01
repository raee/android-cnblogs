package com.rae.cnblogs.sdk.model;

/**
 * 评论
 * 
 * @author ChenRui
 * 
 */
public class Comment
{
	private String	author;
	private String	content;
	private String	date;
	
	public String getAuthor()
	{
		return author;
	}
	
	public void setAuthor(String author)
	{
		this.author = author;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
}
