package com.cnblogs.sdk.model;

import android.text.TextUtils;

public class Blog {
	private String	id;
	private String	title;
	private String	content;
	private String	summary;
	private String	commentCount;
	private String	viewCount;
	private String	cateId;
	private String	autor;			// 作者
	private String	postDate;		// 发布日期
	private String	url;
	private String	blogApp;
	private boolean	isReaded;

	// 解析
	public static Blog parse(String html) {

		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return isNullOrEmpty(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return isNullOrEmpty(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSummary() {
		return isNullOrEmpty(summary);
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getViewCount() {
		return viewCount;
	}

	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}

	public String getAutor() {
		return isNullOrEmpty(autor);
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getPostDate() {
		return isNullOrEmpty(postDate);
	}

	public void setPostDate(String postDate) {
		this.postDate = postDate;
	}

	public String getCateId() {
		return isNullOrEmpty(cateId);
	}

	public void setCateId(String cateId) {
		this.cateId = cateId;
	}

	public String getUrl() {
		return isNullOrEmpty(url);
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBlogApp() {
		return isNullOrEmpty(blogApp);
	}

	public void setBlogApp(String blogApp) {
		this.blogApp = blogApp;
	}

	public boolean isReaded() {
		return isReaded;
	}

	protected String isNullOrEmpty(String str) {
		return TextUtils.isEmpty(str) ? "" : str;
	}

	public void setReaded(boolean isReader) {
		this.isReaded = isReader;
	}
}