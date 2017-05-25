//package rae.com.cnblogs.sdk;
//
//import android.support.test.runner.AndroidJUnit4;
//
//import com.rae.cnblogs.sdk.api.IBookmarksApi;
//import com.rae.cnblogs.sdk.bean.BookmarksBean;
//import com.rae.cnblogs.sdk.impl.WebBookmarksApiImpl;
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
//public class WebBookmarksApiTest extends BaseTest {
//    IBookmarksApi mApi;
//
//    @Override
//    @Before
//    public void setup() {
//        super.setup();
//        mApi = new WebBookmarksApiImpl(mContext);
//    }
//
//    @Test
//    public void testAddBookmarks() throws InterruptedException {
//        startTest(new Runnable() {
//            @Override
//            public void run() {
//                BookmarksBean m = new BookmarksBean("Web接口收藏测试from app", "我是描述信息-rae", "http://www.baidu.com/a");
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
//                mApi.delBookmarks("http://www.baidu.com/a", listener(Void.class));
//            }
//        });
//    }
//}
