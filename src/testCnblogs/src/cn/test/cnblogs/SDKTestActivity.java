package cn.test.cnblogs;

import java.util.List;

import com.cnblogs.sdk.CnblogJsonFactory;
import com.cnblogs.sdk.CnblogUiError;
import com.cnblogs.sdk.CnblogUiListener;
import com.cnblogs.sdk.Cnblogs;
import com.cnblogs.sdk.model.Blog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class SDKTestActivity extends Activity implements OnClickListener {
	private int		page	= 0;
	private String	tag		= "CnblogSdkTest";
	private Cnblogs	cnblog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_blogs);
		findViewById(R.id.button1).setOnClickListener(this);

		cnblog = new Cnblogs(this);
		cnblog.setCnblogUiListener(new CnblogUiListener() {

			@Override
			public void onSuccess(String json) {
				List<Blog> blogs = CnblogJsonFactory.parserBlogs(json);
				for (Blog blog : blogs) {
					Log.i(tag, blog.getTitle());
				}
			}

			@Override
			public void onLoadding(int progress, int totalSize) {
				Log.d(tag, "正在加载：" + progress);

			}

			@Override
			public void onError(CnblogUiError error) {
				Log.e(tag, error.toString());
			}
		});

	}

	@Override
	public void onClick(View v) {
		cnblog.getBlogs("cate/android", page, "", "");
		page++;
	}
}
