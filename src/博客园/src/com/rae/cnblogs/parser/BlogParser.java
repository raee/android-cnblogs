package com.rae.cnblogs.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.rae.cnblogs.model.Blog;

public class BlogParser implements IBlogParser
{
	
	@Override
	public List<Blog> onParse(String xml)
	{
		try
		{
			List<Blog> result = new ArrayList<Blog>();
			XmlPullParser parser = Xml.newPullParser();
			InputStream inputStream = new StringBufferInputStream(xml);
			parser.setInput(inputStream, "utf-8");
			int type = parser.getEventType();
			Blog model = null;
			while (type != XmlPullParser.END_DOCUMENT)
			{
				switch (type)
				{
					case XmlPullParser.START_DOCUMENT:
						Log.i("cnblogs", "文档开始");
						break;
					case XmlPullParser.START_TAG:
						Log.i("cnblogs", "解析标签：" + parser.getName());
						model = parseTag(parser, model);
						break;
					case XmlPullParser.TEXT:
						Log.i("cnblogs", "解析内容：" + parser.getName());
						break;
					case XmlPullParser.END_TAG:
						if (model != null && "entry".equals(parser.getName()))
						{
							result.add(model);
							model = null;
						}
						break;
					
					default:
						break;
				}
				type = parser.next();
			}
			return result;
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private Blog parseTag(XmlPullParser parser, Blog blog)
			throws XmlPullParserException, IOException
	{
		String name = parser.getName(); //标签名
		if ("entry".equalsIgnoreCase(name) && blog == null)
		{
			blog = new Blog();
		}
		if (blog == null) return blog;
		
		if ("id".equalsIgnoreCase(name))
		{
			blog.setId(parser.nextText());
		}
		else if ("title".equalsIgnoreCase(name))
		{
			blog.setTitle(parser.nextText());
		}
		else if ("summary".equalsIgnoreCase(name))
		{
			blog.setSummary(parser.getText());
		}
		else if ("updated".equalsIgnoreCase(name))
		{
			blog.setPostDate(parser.nextText());
		}
		return blog;
	}
	
}
