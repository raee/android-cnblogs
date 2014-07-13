package com.rae.cnblogs.parser;

import java.util.List;

import com.rae.cnblogs.i.BlogException;

/**
 * 博客解析
 * 
 * @author admin
 * 
 */
public interface IBlogParser<T>
{
	List<T> onParse(String xml) throws BlogException;
}
