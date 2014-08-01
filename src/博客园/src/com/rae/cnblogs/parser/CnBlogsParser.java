//package com.rae.cnblogs.parser;
//
//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.xml.sax.Attributes;
//import org.xml.sax.SAXException;
//import org.xml.sax.helpers.DefaultHandler;
//
//import android.util.Log;
//
//import com.rae.cnblogs.i.BlogException;
//import com.rae.cnblogs.model.Blog;
//
//public class CnBlogsParser extends DefaultHandler implements IBlogParser<Blog>
//{
//	private List<Blog>	mBlogs;
//	private Blog		mBlog;
//	private String		mCurrentTagName;
//	
//	@Override
//	public List<Blog> onParse(String xml) throws BlogException
//	{
//		SAXParserFactory factory = SAXParserFactory.newInstance();
//		try
//		{
//			SAXParser parser = factory.newSAXParser();
//			InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
//			parser.parse(inputStream, this);
//			Log.i("cnblogs", "解析数据完成：" + mBlogs.size());
//			return mBlogs;
//		}
//		catch (Exception e)
//		{
//			throw new BlogException(e);
//		}
//	}
//	
//	@Override
//	public void characters(char[] ch, int start, int length) throws SAXException
//	{
//		if (mBlog == null) return;
//		String response = new String(ch, start, length); // 解析的内容
//		if ("id".equalsIgnoreCase(mCurrentTagName))
//		{
//			mBlog.setId(response);
//		}
//		else if ("title".equalsIgnoreCase(mCurrentTagName)) // 标题
//		{
//			mBlog.setTitle(response);
//		}
//		else if ("summary".equalsIgnoreCase(mCurrentTagName)) //摘要
//		{
//			mBlog.setSummary(response);
//		}
//		else if ("published".equalsIgnoreCase(mCurrentTagName)) // 发布日期
//		{
//			mBlog.setPostDate(convertDate(response));
//		}
//		else if ("updated".equalsIgnoreCase(mCurrentTagName)) // 更新日期
//		{
//			mBlog.setPostDate(convertDate(response));
//		}
//		else if ("name".equalsIgnoreCase(mCurrentTagName)) // 作者名字
//		{
//			mBlog.setAutor(response);
//		}
//		else if ("views".equalsIgnoreCase(mCurrentTagName)) // 查看次数
//		{
//			mBlog.setViewCount(Integer.parseInt(response));
//		}
//		else if ("comments".equalsIgnoreCase(mCurrentTagName)) // 评论数
//		{
//			mBlog.setCommentCount(Integer.parseInt(response));
//		}
//	}
//	
//	/**
//	 * 日期转换
//	 * 
//	 * @param date
//	 * @return
//	 */
//	private String convertDate(String date)
//	{
//		
//		date = date.replace('T', ' ').replace('Z', ' ').replace("+08:00", "");
//		return date;
//	}
//	
//	@Override
//	public void endElement(String uri, String localName, String qName) throws SAXException
//	{
//		if (qName.equals("entry") && mBlogs != null && mBlog != null)
//		{
//			mBlogs.add(mBlog);
//			mBlog = null;
//		}
//		mCurrentTagName = qName;
//	}
//	
//	@Override
//	public void startDocument() throws SAXException
//	{
//		mBlogs = new ArrayList<Blog>();
//	}
//	
//	@Override
//	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
//	{
//		if (qName.equalsIgnoreCase("entry"))
//		{
//			this.mBlog = new Blog(); // 实例化一个实体
//		}
//		mCurrentTagName = qName;
//	}
//	
//}
