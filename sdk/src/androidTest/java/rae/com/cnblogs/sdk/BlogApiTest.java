package rae.com.cnblogs.sdk;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 博客测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class BlogApiTest extends BaseTest {

    private IBlogApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = getApiProvider().getBlogApi();
    }

    /**
     * 获取分类
     */
    @Test
    public void testCategory() throws InterruptedException {
        ICategoryApi api = getApiProvider().getCategoriesApi();
        runTest("testCategory", api.getCategories());
    }

    /**
     * 首页博客列表
     */
    @Test
    public void testHomeBlogs() throws InterruptedException {
        runTest("testHomeBlogs", mApi.getBlogList(1, null, null, null));
    }

    /**
     * 博文内容
     */
    @Test
    public void testContent() throws InterruptedException {
        runTest("testContent", mApi.getBlogContent("6246780"));
    }

    /**
     * 博客评论列表
     */
    @Test
    public void testComment() throws InterruptedException {
        runTest("testComment", mApi.getBlogComments(1, "6134506", "pengze0902"));
    }

    /**
     * 知识库列表
     */
    @Test
    public void testKB() throws InterruptedException {
        runTest("testKB", mApi.getKbArticles(1));
    }

    /**
     * 知识库内容
     */
    @Test
    public void testKBContent() throws InterruptedException {
        runTest("testKBContent", mApi.getKbContent("569056"));
    }

    /**
     * 博文点赞
     */
    @Test
    public void testLikeBlog() throws InterruptedException {
        runTest("testLikeBlog", mApi.likeBlog("6323406", "silenttiger"));
    }

    /**
     * 取消博文点赞
     */
    @Test
    public void testUnLikeBlog() throws InterruptedException {
        runTest("testUnLikeBlog", mApi.unLikeBlog("6323406", "silenttiger"));
    }

    /**
     * 知识库点赞
     */
    @Test
    public void testLikeKb() throws InterruptedException {
        runTest("testLikeKb", mApi.likeKb("569992"));
    }

    /**
     * 发表博客评论
     */
    @Test
    public void testAddCommentBlog() throws InterruptedException {
        runTest("testAddCommentBlog", mApi.addBlogComment("6323406", "silenttiger", (String) null, "test comment"));
    }

    /**
     * 删除博客评论
     */
    @Test
    public void testDelCommentBlog() throws InterruptedException {
        runTest("testDelCommentBlog", mApi.deleteBlogComment("6323406"));
    }
}
