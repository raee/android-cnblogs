package com.rae.cnblogs.sdk.official;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.model.Blog;

/**
 * 博客园的博客xml解析
 * 
 * @author ChenRui
 * 
 */
public class CnblogsXmlParser extends XmlParser<Blog>
{
	private List<Blog>						blogs;
	private Blog							currentBlog;
	private String							currentTagName;
	private CnBlogsCallbackListener<Blog>	listener;
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (currentBlog == null) return;
		String response = new String(ch, start, length); // 解析的内容
		if ("id".equalsIgnoreCase(currentTagName))
		{
			currentBlog.setId(response);
		}
		else if ("title".equalsIgnoreCase(currentTagName)) // 标题
		{
			currentBlog.setTitle(response);
		}
		else if ("summary".equalsIgnoreCase(currentTagName)) //摘要
		{
			currentBlog.setSummary(response);
		}
		else if ("published".equalsIgnoreCase(currentTagName)) // 发布日期
		{
			currentBlog.setPostDate(convertDate(response));
		}
		else if ("updated".equalsIgnoreCase(currentTagName)) // 更新日期
		{
			currentBlog.setPostDate(convertDate(response));
		}
		else if ("name".equalsIgnoreCase(currentTagName)) // 作者名字
		{
			currentBlog.setAutor(response);
		}
		else if ("views".equalsIgnoreCase(currentTagName)) // 查看次数
		{
			currentBlog.setViewCount(response);
		}
		else if ("comments".equalsIgnoreCase(currentTagName)) // 评论数
		{
			currentBlog.setCommentCount(response);
		}
	}
	
	/**
	 * 日期转换
	 * 
	 * @param date
	 * @return
	 */
	private String convertDate(String date)
	{
		
		date = date.replace('T', ' ').replace('Z', ' ').replace("+08:00", "");
		return date;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		if (qName.equals("entry") && blogs != null && currentBlog != null)
		{
			blogs.add(currentBlog); // 添加博客
			currentBlog = null;
		}
		if (qName.equals("feed"))
		{
			listener.onLoadBlogs(blogs);
		}
		currentTagName = qName;
	}
	
	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		this.blogs = new ArrayList<Blog>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equalsIgnoreCase("entry"))
		{
			this.currentBlog = new Blog(); // 实例化一个实体
		}
		currentTagName = qName;
	}
}
