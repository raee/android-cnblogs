package com.rae.cnblogs.sdk.official;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.rae.cnblogs.sdk.model.Comment;

/**
 * 博客园的博客xml解析
 * 
 * @author ChenRui
 * 
 */
public class CommentXmlParser extends XmlParser<Comment>
{
	private List<Comment>	comments;
	private Comment			currentComment;
	private String			currentTagName;
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException
	{
		if (currentComment == null) return;
		String response = new String(ch, start, length); // 解析的内容
		if ("name".equalsIgnoreCase(currentTagName))
		{
			currentComment.setAuthor(response);
		}
		else if ("updated".equalsIgnoreCase(currentTagName))
		{
			currentComment.setDate(convertDate(response));
		}
		else if ("content".equalsIgnoreCase(currentTagName))
		{
			currentComment.setContent(response);
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
		if (qName.equals("entry") && comments != null && currentComment != null)
		{
			comments.add(currentComment); // 添加博客
			currentComment = null;
		}
		if (qName.equals("feed"))
		{
			listener.onLoadBlogs(comments);
		}
		currentTagName = qName;
	}
	
	@Override
	public void startDocument() throws SAXException
	{
		super.startDocument();
		this.comments = new ArrayList<Comment>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
	{
		if (qName.equalsIgnoreCase("entry"))
		{
			this.currentComment = new Comment(); // 实例化一个实体
		}
		currentTagName = qName;
	}
}
