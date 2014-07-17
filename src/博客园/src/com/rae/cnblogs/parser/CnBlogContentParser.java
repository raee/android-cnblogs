package com.rae.cnblogs.parser;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.text.Html;
import android.util.Log;
import android.util.Xml;

import com.rae.cnblogs.i.BlogException;
import com.rae.cnblogs.model.Blog;

public class CnBlogContentParser implements IBlogParser<Blog>
{
	
	@SuppressWarnings("deprecation")
	@Override
	public List<Blog> onParse(String xml) throws BlogException
	{
		List<Blog> result = new ArrayList<Blog>();
		XmlPullParser parser = Xml.newPullParser();
		Blog m = null;
		try
		{
			parser.setInput(new StringReader(xml));
			int eventType = 0;
			while ((eventType = parser.next()) != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType)
				{
					case XmlPullParser.TEXT:
						if (m != null) m.setContent(parser.getText());
						break;
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equals("string"))
						{
							m = new Blog();
							result.add(m);
						}
						break;
					default:
						break;
				}
			}
			
		}
		catch (Exception e)
		{
			throw new BlogException(e);
		}
		return result;
	}
	
}
