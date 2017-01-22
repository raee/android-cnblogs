package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;
import android.webkit.CookieManager;

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
        mApi = getApiProvider().getBlogApi();
        mCategoryApi = getApiProvider().getCategoryApi();

        // 模拟已经登录
        CookieManager.getInstance().setCookie("http://www.cnblogs.com", ".CNBlogsCookie=8C6D967C837C2CCBABCEC7567EE8AB7D44FE79BF1D0FC1219DD216AA34DE6B1A1FA8656DFCFC7370985A3EB177A82942FBCB5B3797B975F4DA582590059D123417B74C2FC28647F5541DD7F873BCEE5B4B627E3B");

    }

    @Test
    public void testCategory() throws InterruptedException {
        startTest(new Runnable() {
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
        startTest(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Test
    public void testContent() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getBlogContent("6246780", new ApiUiListener<String>() {
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
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getBlogComments(1, "6134506", "pengze0902", new ApiUiArrayListener<BlogComment>() {
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

    @Test
    public void testLikeBlog() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                getApiProvider().getBlogApi().likeBlog("6323406", "silenttiger", listener(Void.class));
            }
        });
    }

    @Test
    public void testUnLikeBlog() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                getApiProvider().getBlogApi().unLikeBlog("6323406", "silenttiger", listener(Void.class));
            }
        });
    }

    @Test
    public void testAddCommentBlog() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                // 普通
                getApiProvider().getBlogApi().addBlogComment("6323406", "silenttiger", (String) null, "test comment", listener(Void.class));
            }
        });
    }

    @Test
    public void testDelCommentBlog() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                getApiProvider().getBlogApi().deleteBlogComment("3608338", listener(Void.class));
            }
        });
    }

}
