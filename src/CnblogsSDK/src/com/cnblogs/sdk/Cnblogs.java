package com.cnblogs.sdk;

import com.cnblogs.sdk.model.Blog;
import com.rae.core.http.async.RequestParams;

import android.content.Context;

/**
 * 博客园接口
 * 
 * <pre>
 * 需要权限：
 * {@link android.Manifest.permission#INTERNET INTERNET}
 * 
 * @author ChenRui
 * 
 */
public class Cnblogs {
	private CnblogUiListener	listener;
	private String				baseUrl	= "http://cnblogs.davismy.com/Handler.ashx";

	// private Context context;

	public Cnblogs(Context context) {
		// this.context = context;
	}

	/**
	 * 设置监听
	 * 
	 * @param l
	 */
	public void setCnblogUiListener(CnblogUiListener l) {
		this.listener = l;
	}

	public void getBlogs(String cateId, int page, String sinceId, String maxId) {
		HttpJsonClient client = new HttpJsonClient(listener);
		RequestParams params = new RequestParams();
		params.put("op", "GetTimeLine");
		params.put("page", page);
		params.put("since_id", sinceId);
		params.put("max_id", maxId);
		params.put("channelpath", cateId);
		params.put("t", System.currentTimeMillis());
		client.getJson(baseUrl, params);
	}

	public void getBlogs(int page) {
		getBlogs("", page);
	}

	public void getBlogs(String cateId, int page) {
		getBlogs(cateId, page, "", "");
	}

	public void getContent(Blog blog) {
		if (blog == null) {
			notifyError("0001", "博客不能为空，请求取消！");
			return;
		}

		HttpJsonClient client = new HttpJsonClient(listener);
		RequestParams params = new RequestParams();
		params.put("op", "GetBlogContent");
		params.put("blog_id", blog.getId());
		client.getJson(baseUrl, params);
	}

	private void notifyError(String errorCode, String message) {
		if (listener != null) {
			listener.onError(new CnblogUiError(errorCode, message));
		}
	}

}
