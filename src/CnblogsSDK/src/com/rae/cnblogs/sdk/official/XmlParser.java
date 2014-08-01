package com.rae.cnblogs.sdk.official;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.helpers.DefaultHandler;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;

public abstract class XmlParser<T> extends DefaultHandler
{
	protected CnBlogsCallbackListener<T>	listener;
	
	public void parser(CnBlogsCallbackListener<T> l, String xml)
	{
		try
		{
			this.listener = l;
			ByteArrayInputStream inByte = new ByteArrayInputStream(xml.getBytes());
			BufferedInputStream inputStream = new BufferedInputStream(inByte);
			SAXParserFactory.newInstance().newSAXParser().parse(inputStream, this);
		}
		catch (Exception e)
		{
			listener.onLoadError(new CnBlogsException("XML解析博客错误！", e));
		}
	}
}
