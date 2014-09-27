package com.rae.cnblogs.sdk;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.data.IDataProvider;
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
public class CnBlogsOfficialOpenAPI extends CnBlogsOpenAPI {
	private BlogDownload mBlogDownloader;
	private CommentDownloader mCommentDownloader;
	private ContentDownloader mContentDownloader;
	private CnBlogsOtherOpenAPI mOtherSdk;
	private IDataProvider mDbProvider;

	public CnBlogsOfficialOpenAPI(Context context) {
		super(context);
		mDbProvider = getDataProvider();
	}

	@Override
	protected Downloader<Blog> getBlogDownloader() {
		if (mBlogDownloader == null)
			mBlogDownloader = new BlogDownload(mContext);
		return mBlogDownloader;
	}

	@Override
	public void getCnblogs(CnBlogsCallbackListener<Blog> l, int index) {
		super.getCnblogsByUrl(l, Constant.CNBLOGS_PAGE, index);
	}

	@Override
	public void getRecentCnblogs(CnBlogsCallbackListener<Blog> l, int index) {
		super.getCnblogsByUrl(l, Constant.CNBLOGS_RECENT, index);
	}

	@Override
	public void getTenDaysTopDiggPosts(CnBlogsCallbackListener<Blog> l, int size) {
		super.getCnblogsByUrl(l, Constant.CNBLOGS_DAY_READING, size);
	}

	@Override
	public void getRecommend(CnBlogsCallbackListener<Blog> l, int index) {
		super.getCnblogsByUrl(l, Constant.CNBLOGS_RECOMMENT_PAGE, index);
	}

	@Override
	public void get48HoursTopViewPosts(CnBlogsCallbackListener<Blog> l, int size) {
		super.getCnblogsByUrl(l, Constant.CNBLOGS_HOUR_READING, size);
	}

	@Override
	public void getComments(CnBlogsCallbackListener<Comment> l, String blogId,
			int index) {
		getCommentDownloader().setOnCnBlogCallbackListener(l);
		getCommentDownloader().download(Constant.CNBLOGS_COMMENETS_PAGE,
				blogId, index, mPageSize);
	}

	@Override
	protected Downloader<Comment> getCommentDownloader() {
		if (mCommentDownloader == null)
			mCommentDownloader = new CommentDownloader(mContext);
		return mCommentDownloader;
	}

	private CnBlogsOtherOpenAPI getOtherSdk() {
		if (mOtherSdk == null) {
			mOtherSdk = new CnBlogsOtherOpenAPI(mContext);
		}
		return mOtherSdk;
	}

	@Override
	public void getCnblogs(CnBlogsCallbackListener<Blog> l, String cateId,
			String blogId, int index) {
		getOtherSdk().getCnblogs(l, index, blogId, blogId, cateId);
	}

	@Override
	public void getBlogContent(CnBlogsCallbackListener<Blog> l, String blogId) {
		// 先从数据库获取，没有在从网络获取
		Blog blog = mDbProvider.getBlog(blogId);
		if (blog == null) {
			blog = new Blog();
			blog.setId(blogId);
		} else if (!TextUtils.isEmpty(blog.getContent())) {
			List<Blog> result = new ArrayList<Blog>();
			result.add(blog);
			l.onLoadBlogs(result);
			return;
		}

		if (mContentDownloader == null) {
			mContentDownloader = new ContentDownloader(mContext);
			mContentDownloader.setOnCnBlogCallbackListener(l);
		}
		mContentDownloader.download(Constant.CNBLOGS_CONTENT, blog);
	}
}
