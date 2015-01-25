package com.cnblogs.sdk;

import java.util.List;

import com.cnblogs.sdk.model.Blog;

public final class CnblogJsonFactory {
	public static List<Blog> parserBlogs(String json) {
		return new BlogJsonParser().parser(json);
	}
}
