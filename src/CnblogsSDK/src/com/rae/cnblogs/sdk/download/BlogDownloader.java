package com.rae.cnblogs.sdk.download;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.http.Downloader;
import com.rae.cnblogs.sdk.model.Blog;

public class BlogDownloader extends Downloader<Blog>
{
	private XmlParser	mXmlParser;
	private List<Blog>	mBlogs;
	
	public BlogDownloader(Context context)
	{
		super(context);
		mXmlParser = new XmlParser();
	}
	
	@Override
	public void onHttpResponse(String html)
	{
		try
		{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			InputStream inputStream = new ByteArrayInputStream(html.getBytes());
			parser.parse(inputStream, mXmlParser);
		}
		catch (Exception e)
		{
			super.onHttpRequestError(new CnBlogsException(e));
		}
	}
	
	class XmlParser extends DefaultHandler
	{
		
		private Blog	mBlog;
		private String	mCurrentTagName;
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException
		{
			if (mBlog == null) return;
			String response = new String(ch, start, length); // 解析的内容
			if ("id".equalsIgnoreCase(mCurrentTagName))
			{
				mBlog.setId(response);
			}
			else if ("title".equalsIgnoreCase(mCurrentTagName)) // 标题
			{
				mBlog.setTitle(response);
			}
			else if ("summary".equalsIgnoreCase(mCurrentTagName)) //摘要
			{
				mBlog.setSummary(response);
			}
			else if ("published".equalsIgnoreCase(mCurrentTagName)) // 发布日期
			{
				mBlog.setPostDate(convertDate(response));
			}
			else if ("updated".equalsIgnoreCase(mCurrentTagName)) // 更新日期
			{
				mBlog.setPostDate(convertDate(response));
			}
			else if ("name".equalsIgnoreCase(mCurrentTagName)) // 作者名字
			{
				mBlog.setAutor(response);
			}
			else if ("views".equalsIgnoreCase(mCurrentTagName)) // 查看次数
			{
				mBlog.setViewCount(response);
			}
			else if ("comments".equalsIgnoreCase(mCurrentTagName)) // 评论数
			{
				mBlog.setCommentCount(response);
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
			//			info("end:" + qName);
			if (qName.equals("entry") && mBlogs != null && mBlog != null)
			{
				mBlogs.add(mBlog);
				info("添加博客：" + mBlog.getTitle());
				mBlog = null;
			}
			if (qName.equals("feed"))
			{
				onCallback(mBlogs); // 解析文档结束，解析完成！！
			}
			mCurrentTagName = qName;
		}
		
		@Override
		public void startDocument() throws SAXException
		{
			mBlogs = new ArrayList<Blog>();
			info("开始解析数据...");
		}
		
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
		{
			if (qName.equalsIgnoreCase("entry"))
			{
				this.mBlog = new Blog(); // 实例化一个实体
			}
			mCurrentTagName = qName;
		}
	}
	
}
