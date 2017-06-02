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
        CookieManager.getInstance().setCookie("http://www.cnblogs.com", ".CNBlogsCookie=CDFE7816E5AE5E642714451EDD29C27281CBE721475FBB78475E9E0E0FD6530161F251A2802A4CA286F22366101E8DDC47C30D8A5AD8FB183BDBE1709FD2EFA0E08860ABE10D1EE42437F4D00757CFDD39630464;");

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

    @Test
    public void testUnLikeBlog() throws InterruptedException {
        runTest("testUnLikeBlog", mApi.unLikeBlog("6323406", "silenttiger"));
    }

    @Test
    public void testLikeKb() throws InterruptedException {
        runTest("testLikeKb", mApi.likeKb("569992"));
    }

    @Test
    public void testAddCommentBlog() throws InterruptedException {
        runTest("testAddCommentBlog", mApi.addBlogComment("6323406", "silenttiger", (String) null, "test comment"));
    }

    @Test
    public void testDelCommentBlog() throws InterruptedException {
        runTest("testDelCommentBlog", mApi.deleteBlogComment("6323406"));
    }
}
