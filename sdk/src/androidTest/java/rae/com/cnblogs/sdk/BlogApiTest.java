package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.ICategoryApi;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

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
    private ICategoryApi mCategoryApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getBlogApi(mContext);
        mCategoryApi = CnblogsApiFactory.getCategoryApi(mContext);
    }

    @Test
    public void testCategory() throws InterruptedException {
        run(new Runnable() {
            @Override
            public void run() {
                mCategoryApi.getCategory(new ApiUiArrayListener<Category>() {
                    @Override
                    public void onApiFailed(ApiException ex, String msg) {
                        error(ex);
                        stop();
                    }

                    @Override
                    public void onApiSuccess(List<Category> data) {
                        for (Category blog : data) {
                            log("%s --> %s", blog.getName(), blog.getCategoryId());
                        }
                        stop();
                    }
                });
            }
        });
    }

    @Test
    public void testHomeBlogs() throws InterruptedException {
        run(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Test
    public void testContent() throws InterruptedException {
        run(new Runnable() {
            @Override
            public void run() {
                mApi.getContents("6105103", new ApiUiListener<String>() {
                    @Override
                    public void onApiFailed(ApiException ex, String msg) {
                        stop();
                    }

                    @Override
                    public void onApiSuccess(String data) {
                        log(data);
                        stop();
                    }
                });
            }
        });
    }

    @Test
    public void testComment() throws InterruptedException {
        run(new Runnable() {
            @Override
            public void run() {
                mApi.getComment(1, "6134506", "pengze0902", new ApiUiArrayListener<BlogComment>() {
                    @Override
                    public void onApiFailed(ApiException ex, String msg) {
                        stop();
                    }

                    @Override
                    public void onApiSuccess(List<BlogComment> data) {

                        for (BlogComment comment : data) {
                            log(comment.getBody());
                        }
                        stop();
                    }
                });
            }
        });
    }
}
