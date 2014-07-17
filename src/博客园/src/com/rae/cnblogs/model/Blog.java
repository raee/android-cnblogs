package com.rae.cnblogs.model;

/**
 * 博客
 * 
 * @author admin
 * 
 */
public class Blog
{
	private String	id;
	private String	title;
	private String	content;
	private String	summary;
	private int		commentCount;
	private int		viewCount;
	private String	cateId;
	private String	autor;			//作者
	private String	postDate;		//发布日期
									
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	public String getSummary()
	{
		return summary;
	}
	
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	
	public int getCommentCount()
	{
		return commentCount;
	}
	
	public void setCommentCount(int commentCount)
	{
		this.commentCount = commentCount;
	}
	
	public int getViewCount()
	{
		return viewCount;
	}
	
	public void setViewCount(int viewCount)
	{
		this.viewCount = viewCount;
	}
	
	public String getAutor()
	{
		return autor;
	}
	
	public void setAutor(String autor)
	{
		this.autor = autor;
	}
	
	public String getPostDate()
	{
		return postDate;
	}
	
	public void setPostDate(String postDate)
	{
		this.postDate = postDate;
	}
	
	public String getCateId()
	{
		return cateId;
	}
	
	public void setCateId(String cateId)
	{
		this.cateId = cateId;
	}
}
