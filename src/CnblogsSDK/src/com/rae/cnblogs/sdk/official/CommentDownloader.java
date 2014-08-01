package com.rae.cnblogs.sdk.official;

import android.content.Context;

import com.rae.cnblogs.sdk.download.Downloader;
import com.rae.cnblogs.sdk.model.Comment;

public class CommentDownloader extends Downloader<Comment>
{
	private CommentXmlParser	parser	= new CommentXmlParser();
	
	public CommentDownloader(Context context)
	{
		super(context);
	}
	
	@Override
	public void onHttpResponse(String html)
	{
		parser.parser(getListener(), html);
	}
	
}
