package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.download.Downloader;
import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Comment;
import com.rae.cnblogs.sdk.model.Constant;
import com.rae.cnblogs.sdk.official.BlogDownload;
import com.rae.cnblogs.sdk.official.CommentDownloader;
import com.rae.cnblogs.sdk.official.ContentDownloader;

/**
 * 官方博客园接口
 * 
 * @author ChenRui
 * 
 */
public class CnBlogsOfficialOpenAPI extends CnBlogsOpenAPI
{
	private Downloader<Blog>	mBlogDownloader;
	private Downloader<Comment>	mCommentDownloader;
	private Downloader<Blog>	mContentDownloader;
	private CnBlogsOtherOpenAPI	mOtherSdk;
	
	public CnBlogsOfficialOpenAPI(Context context)
	{
		super(context);
		
	}
	
	@Override
	protected Downloader<Blog> getBlogDownloader()
	{
		if (mBlogDownloader == null) mBlogDownloader = new BlogDownload(mContext);
		return mBlogDownloader;
	}
	
	@Override
	public void getCnblogs(CnBlogsCallbackListener<Blog> l, int index)
	{
		super.getCnblogsByUrl(l, Constant.CNBLOGS_PAGE, index);
	}
	
	@Override
	public void getRecentCnblogs(CnBlogsCallbackListener<Blog> l, int index)
	{
		super.getCnblogsByUrl(l, Constant.CNBLOGS_RECENT, index);
	}
	
	@Override
	public void getTenDaysTopDiggPosts(CnBlogsCallbackListener<Blog> l, int size)
	{
		super.getCnblogsByUrl(l, Constant.CNBLOGS_DAY_READING, size);
	}
	
	@Override
	public void getRecommend(CnBlogsCallbackListener<Blog> l, int index)
	{
		super.getCnblogsByUrl(l, Constant.CNBLOGS_RECOMMENT_PAGE, index);
	}
	
	@Override
	public void get48HoursTopViewPosts(CnBlogsCallbackListener<Blog> l, int size)
	{
		super.getCnblogsByUrl(l, Constant.CNBLOGS_HOUR_READING, size);
	}
	
	@Override
	public void getComments(CnBlogsCallbackListener<Comment> l, String blogId, int index)
	{
		getCommentDownloader().setOnCnBlogCallbackListener(l);
		getCommentDownloader().download(Constant.CNBLOGS_COMMENETS_PAGE, blogId, index, mPageSize);
	}
	
	@Override
	protected Downloader<Comment> getCommentDownloader()
	{
		if (mCommentDownloader == null) mCommentDownloader = new CommentDownloader(mContext);
		return mCommentDownloader;
	}
	
	private CnBlogsOtherOpenAPI getOtherSdk()
	{
		if (mOtherSdk == null)
		{
			mOtherSdk = new CnBlogsOtherOpenAPI(mContext);
		}
		return mOtherSdk;
	}
	
	@Override
	public void getCnblogs(CnBlogsCallbackListener<Blog> l, String cateId, String blogId, int index)
	{
		getOtherSdk().getCnblogs(l, index, blogId, blogId, cateId);
	}
	
	@Override
	public void getBlogContent(CnBlogsCallbackListener<Blog> l, String blogId)
	{
		if (mContentDownloader == null)
		{
			mContentDownloader = new ContentDownloader(mContext);
		}
		mContentDownloader.setOnCnBlogCallbackListener(l);
		mContentDownloader.download(Constant.CNBLOGS_CONTENT, blogId);
	}
}
