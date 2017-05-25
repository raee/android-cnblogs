//package rae.com.cnblogs.sdk;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.rae.cnblogs.sdk.api.IBookmarksApi;
//import com.rae.cnblogs.sdk.bean.BookmarksBean;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
///**
// * 用户接口测试
// * Created by ChenRui on 2017/1/14 01:09.
// */
//@RunWith(AndroidJUnit4.class)
//public class BookmarksApiTest extends BaseTest {
//    IBookmarksApi mApi;
//
//    @Override
//    @Before
//    public void setup() {
//        super.setup();
//        mApi = getApiProvider().getBookmarksApi();
//    }
//
//    @Test
//    public void testAddBookmarks() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                BookmarksBean m = new BookmarksBean("百度收藏测试from app", "我是描述信息-rae", "http://www.baidu.com");
//                mApi.addBookmarks(m, listener(Void.class));
//            }
//        });
//    }
//
//    @Test
//    public void testGetBookmarks() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                mApi.getBookmarks(1, listListener(BookmarksBean.class));
//            }
//        });
//    }
//
//    @Test
//    public void testDeleteBookmarks() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                mApi.delBookmarks("http://www.baidu.com", listener(Void.class));
//            }
//        });
//    }
//}
