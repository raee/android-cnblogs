package com.cnblogs.sdk.model;

/**
 * 博客分类
 * 
 * @author ChenRui
 * 
 */
public class Category {
	private String	id;
	private String	name;
	private String	parentName; // 

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	private boolean	isShow;

	public Category() {
	}

	public Category(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
}
