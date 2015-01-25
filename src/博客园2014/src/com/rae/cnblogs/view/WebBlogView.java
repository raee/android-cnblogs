package com.rae.cnblogs.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rae.cnblogs.sdk.model.Blog;

public class WebBlogView extends WebView {
	private Blog mCurrentBlog;

	public WebBlogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		WebSettings setting = this.getSettings();
		setting.setJavaScriptEnabled(true);
		addJavascriptInterface(this, "android");
		this.setWebChromeClient(new WebChromeClient());
		this.setWebViewClient(new WebViewClient());
	}

	public void load(Blog blog) {
		this.mCurrentBlog = blog;
		// String url = "http://192.168.1.2/test/view.html"; //
		String url = "file:///android_asset/view.html";
		this.loadUrl(url);
	}

	@JavascriptInterface
	public String getBlog() {
		try {
			JSONObject obj = new JSONObject();
			obj.put("title", mCurrentBlog.getTitle());
			obj.put("content", mCurrentBlog.getContent());
			obj.put("author", mCurrentBlog.getAutor());
			obj.put("date", mCurrentBlog.getPostDate());
			obj.put("view", mCurrentBlog.getViewCount());
			obj.put("comment", mCurrentBlog.getCommentCount());
			return obj.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

}
