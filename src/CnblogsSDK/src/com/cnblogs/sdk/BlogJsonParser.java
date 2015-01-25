package com.cnblogs.sdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cnblogs.sdk.model.Blog;

/**
 * 博客JSON解析
 * 
 * @author ChenRui
 * 
 */
class BlogJsonParser extends JsonParser<Blog> {

	@Override
	public List<Blog> parser(String json) {
		try {
			JSONArray arr = getJsonData(json);
			List<Blog> blogs = new ArrayList<Blog>();
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Blog blog = new Blog();
				blog.setAutor(obj.getString("author"));
				blog.setBlogApp(obj.getString("blogapp"));
				blog.setCateId(""); // 分类
				blog.setCommentCount(obj.getString("comment"));
				blog.setId(obj.getString("blog_id"));
				blog.setPostDate(obj.getString("public_time"));
				blog.setSummary(obj.getString("content"));
				blog.setTitle(obj.getString("title"));
				blog.setViewCount(obj.getString("hit"));
				blogs.add(blog);
			}
			return blogs;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
