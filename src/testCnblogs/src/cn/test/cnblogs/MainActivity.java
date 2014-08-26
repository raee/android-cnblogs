package cn.test.cnblogs;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.CnBlogsOfficialOpenAPI;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.data.DataProvider;
import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Comment;

public class MainActivity extends Activity implements CnBlogsCallbackListener<Blog>, OnClickListener
{
	final CnBlogsOpenAPI	sdk	= new CnBlogsOfficialOpenAPI(this);
	DataProvider			db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DataProvider(MainActivity.this);
		test();
	}
	
	private void test()
	{
		
	}
	
	void log(Object obj)
	{
		obj = obj == null ? "" : obj;
		Log.i("cnblogtest", obj.toString());
	}
	
	void error(Object obj)
	{
		obj = obj == null ? "" : obj;
		Log.e("cnblogtest", obj.toString());
	}
	
	@Override
	public void onLoadError(CnBlogsException e)
	{
		e.printStackTrace();
		error(e.getMessage());
	}
	
	@Override
	public void onLoadBlogs(List<Blog> result)
	{
		for (Blog blog : result)
		{
			log("标题：" + blog.getTitle());
		}
	}
	
	int	index	= 0;
	
	@Override
	public void onClick(View v)
	{
		log(((Button) v).getText());
		switch (v.getId())
		{
			case R.id.Button01:
				sdk.getTenDaysTopDiggPosts(this, 20);
				break;
			case R.id.Button02:
				sdk.getRecentCnblogs(this, index);
				break;
			case R.id.Button03:
				sdk.getCnblogs(this, index);
				break;
			case R.id.Button04:
				sdk.getRecommend(this, index);
				break;
			case R.id.Button05:
				sdk.get48HoursTopViewPosts(this, 20);
				break;
			case R.id.Button06:
				sdk.getComments(new CnBlogsCallbackListener<Comment>()
				{
					
					@Override
					public void onLoadError(CnBlogsException e)
					{
						error(e.getMessage());
					}
					
					@Override
					public void onLoadBlogs(List<Comment> result)
					{
						for (Comment comment : result)
						{
							log(comment.getAuthor() + ":" + comment.getContent());
						}
					}
				}, "3878591", 1);
				break;
			default:
				break;
		}
		index++;
	}
	
}
