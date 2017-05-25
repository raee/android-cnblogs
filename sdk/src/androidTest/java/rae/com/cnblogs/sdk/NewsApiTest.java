//package rae.com.cnblogs.sdk;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.rae.cnblogs.sdk.bean.BlogBean;
//import com.rae.cnblogs.sdk.bean.BlogCommentBean;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
///**
// * Created by ChenRui on 2016/11/30 00:15.
// */
//@RunWith(AndroidJUnit4.class)
//public class NewsApiTest extends BaseTest {
//
//    @Test
//    public void testNews() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getNewsApi().getNews(1, listListener(BlogBean.class));
//            }
//        });
//    }
//
//    @Test
//    public void testNewsContent() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getNewsApi().getNewsContent("561600", listener(String.class));
//            }
//        });
//    }
//
//    @Test
//    public void testAddNewsComment() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getNewsApi().addNewsComment("561600", (String) null, "test new comment!", listener(Void.class));
//            }
//        });
//    }
//
//    @Test
//    public void testAddNewsCommentWithReplay() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                BlogCommentBean comment = new BlogCommentBean();
//                comment.setId("360847");
//                comment.setBlogApp("Rae");
//                comment.setBody("dgfd");
//                getApiProvider().getNewsApi().addNewsComment("561600", comment, "test new comment!", listener(Void.class));
//            }
//        });
//    }
//
//
//    @Test
//    public void testDelNewsComment() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                getApiProvider().getNewsApi().deleteNewsComment("561600", "360854", listener(Void.class));
//            }
//        });
//    }
//}
