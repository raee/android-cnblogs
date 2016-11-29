package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.ICnblogsListener;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.sdk.exception.ApiErrorCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidJUnit4.class)
public class BlogApiTest extends BaseTest {

    private IBlogApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getBlogApi(mContext);
    }

    @Test
    public void testHomeBlogs() throws InterruptedException {
        run(new Runnable() {
            @Override
            public void run() {
                mApi.getHomeBlogs(1, new ICnblogsListener<Blog>() {
                    @Override
                    public void onApiSuccess(List<Blog> data) {
                        for (Blog blog : data) {
                            log(blog.getTitle());
                        }
                        stop();
                    }

                    @Override
                    public void onApiError(ApiErrorCode errorCode) {
                        error(errorCode);
                        stop();
                    }
                });
            }
        });
    }
}
