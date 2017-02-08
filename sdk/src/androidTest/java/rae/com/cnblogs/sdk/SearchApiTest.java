package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.ISearchApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ChenRui on 2017/2/7 0007 15:34.
 */
@RunWith(AndroidJUnit4.class)
public class SearchApiTest extends BaseTest {

    private ISearchApi mApi;

    @Override
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getInstance(mContext).getSearchApi();
    }


    @Test
    public void testSuggestion() throws Exception {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getSuggestion("android", listListener(String.class));
            }
        });
    }

    @Test
    public void testSearchBlogAuthor() throws Exception {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.searchBlogAuthor("android", listListener(UserInfoBean.class));
            }
        });
    }

    @Test
    public void testSearchBlogList() throws Exception {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.searchNewsList("android", 2, listListener(BlogBean.class));
            }
        });
    }

}
