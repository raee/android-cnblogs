//package com.rae.cnblogs.sdk;
//
//import android.content.Context;
//import android.text.TextUtils;
//
//import com.rae.cnblogs.sdk.download.ContentDownloader;
//import com.rae.cnblogs.sdk.model.Blog;
//import com.rae.cnblogs.sdk.model.Constant;
//
///**
// * 通过http请求获取博客接口
// * 
// * @author ChenRui
// * 
// */
//public class HttpCnBlogsOpenAPI extends CnBlogsOpenAPI
//{
//	
//	public HttpCnBlogsOpenAPI(Context context)
//	{
//		super(context);
//	}
//	
//	@Override
//	public void getBlogs(int page)
//	{
//		getBlogs(null, page);
//	}
//	
//	@Override
//	public void getBlogs(String cateId, int page)
//	{
//		if (checkBlogApiIsNull())
//		{
//			return;
//		}
//		String url = Constant.BLOG_SITE_HOME_PAGE;
//		cateId = TextUtils.isEmpty(cateId) ? "" : cateId;
//		// page, since, max ,cate
//		mBlogDownload.download(url, page, null, null, cateId);
//	}
//	
//	@Override
//	public void getBlogContent(Blog model)
//	{
//		ContentDownloader contentDownloader = new ContentDownloader(mContext);
//		contentDownloader.setOnCnBlogCallbackListener(mBlogDownload.getCnBlogCallbackListener()); // 监听是和获取博客列表一样
//		if (checkBlogApiIsNull())
//		{
//			return;
//		}
//		//contentDownloader.download(Constant.BLOG_CONTENT, model);
//	}
//}
