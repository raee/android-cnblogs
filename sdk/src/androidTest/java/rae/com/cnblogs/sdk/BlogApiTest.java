package rae.com.cnblogs.sdk;

import android.webkit.CookieManager;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BlogApiTest extends BaseTest {

    private IBlogApi mApi;
//    private ICategoryApi mCategoryApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getBlogApi();
//        mCategoryApi = getApiProvider().getCategoryApi();

        // 模拟已经登录
        CookieManager.getInstance().setCookie("http://www.cnblogs.com", ".CNBlogsCookie=8C6D967C837C2CCBABCEC7567EE8AB7D44FE79BF1D0FC1219DD216AA34DE6B1A1FA8656DFCFC7370985A3EB177A82942FBCB5B3797B975F4DA582590059D123417B74C2FC28647F5541DD7F873BCEE5B4B627E3B");

    }

    @Test
    public void testCategory() throws InterruptedException {
        ICategoryApi api = getApiProvider().getCategoriesApi();
        runTest("testCategory", api.getCategories());
    }

    @Test
    public void testHomeBlogs() throws InterruptedException {
        runTest("testHomeBlogs", mApi.getBlogList(1, null, null, null));
    }

    @Test
    public void testContent() throws InterruptedException {
        runTest("testContent", mApi.getBlogContent("6246780"));
    }

    @Test
    public void testComment() throws InterruptedException {
        runTest("testComment", mApi.getBlogComments(1, "6134506", "pengze0902"));
    }

    @Test
    public void testKB() throws InterruptedException {
        runTest("testKB", mApi.getKbArticles(1));
    }

    @Test
    public void testKBContent() throws InterruptedException {
        runTest("testKBContent", mApi.getKbContent("569056"));
    }

    @Test
    public void testLikeBlog() throws InterruptedException {
        runTest("testLikeBlog", mApi.likeBlog("6323406", "silenttiger"));
    }
//
//    @Test
//    public void testUnLikeBlog() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getBlogApi().unLikeBlog("6323406", "silenttiger", listener(Void.class));
//            }
//        });
//    }
//
//    @Test
//    public void testAddCommentBlog() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                // 普通
//                getApiProvider().getBlogApi().addBlogComment("6323406", "silenttiger", (String) null, "test comment", listener(Void.class));
//            }
//        });
//    }
//
//    @Test
//    public void testDelCommentBlog() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getBlogApi().deleteBlogComment("3608338", listener(Void.class));
//            }
//        });
//    }
}
