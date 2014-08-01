//package com.rae.cnblogs.controller;
//
//import java.util.List;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.rae.cnblogs.data.DataFactory;
//import com.rae.cnblogs.data.DataProvider;
//import com.rae.cnblogs.data.IDataProvider;
//import com.rae.cnblogs.i.BlogFactory;
//import com.rae.cnblogs.i.BlogListener;
//import com.rae.cnblogs.i.AbsBlogOpenAPI;
//import com.rae.cnblogs.model.Blog;
//
///**
// * 博客提供者
// * 
// * @author ChenRui
// * 
// */
//public class BlogProvider
//{
//	private IDataProvider	mDbProvider	= null;
//	private AbsBlogOpenAPI			mBlogApi;
//	public static int		DefaultSize	= 20;
//	
//	public BlogProvider(Context context)
//	{
//		mDbProvider = DataFactory.getDataProvider(context);
//		mBlogApi = BlogFactory.getCnBlogsApi();
//	}
//	
//	/**
//	 * 获取博客列表
//	 * 
//	 * @param cateId
//	 *            博客分类，传入空默认获取首页推荐博客
//	 * @param index
//	 *            页码
//	 * @param listener
//	 *            博客数据回调监听
//	 */
//	public void getBlogList(String cateId, int index, BlogListener<Blog> listener)
//	{
//		if (TextUtils.isEmpty(cateId))
//		{
//			mBlogApi.getHomeBlogs(listener, index, DefaultSize);
//		}
//		else
//		{
//			mBlogApi.getBlogsByCategroy(listener, cateId, index, DefaultSize);
//		}
//	}
//	
//	/**
//	 * @param index
//	 * @return
//	 */
//	public void getNewBlogList(int index)
//	{
//		// 1、本地获取最新的博客Id
//		Blog blog = mDbProvider.getLastNewBlog();
//		
//		// 2、有最新博客Id,根据最新Id获取列表
//		if (blog != null)
//		{
//			// 2-1、获取分页(1,20)，返回结果逐条查询是否已经存在和最新博客Id匹配，如果是，就已经获取完成，记录当前获取的页码，返回临时数据。
//			
//			// 2-2、没有和最新博客Id匹配，继续获取，并将数据保存到临时数据中。页码数递增；
//		}
//		else
//		{
//			// 3、没有最新博客Id(第一次运行程序)，返回第一页数据
//			//mBlogApi.getHomeBlogs(listener, index, size);
//		}
//	}
//}
