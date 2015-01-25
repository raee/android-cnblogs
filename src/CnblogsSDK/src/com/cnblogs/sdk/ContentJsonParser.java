package com.cnblogs.sdk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cnblogs.sdk.model.Blog;

public class ContentJsonParser extends JsonParser<Blog> {

	private Blog	sourceBlog;

	public ContentJsonParser(Blog source) {
		this.sourceBlog = source;
	}

	@Override
	public List<Blog> parser(String json) {
		try {
			JSONObject obj = new JSONObject(json);
			sourceBlog.setContent(obj.getString("data"));
			ArrayList<Blog> blogs = new ArrayList<Blog>();
			blogs.add(sourceBlog);
			return blogs;
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
