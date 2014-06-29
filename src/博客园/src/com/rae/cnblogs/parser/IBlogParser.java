package com.rae.cnblogs.parser;

import java.util.List;

import com.rae.cnblogs.BlogException;
import com.rae.cnblogs.model.Blog;

/**
 * 博客解析
 * 
 * @author admin
 * 
 */
public interface IBlogParser
{
	List<Blog> onParse(String xml) throws BlogException;
}
