package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IFriendsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ChenRui on 2017/2/7 0007 15:34.
 */
@RunWith(AndroidJUnit4.class)
public class FriendApiTest extends BaseTest {

    private IFriendsApi mApi;

    @Override
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getInstance(mContext).getFriendApi();
    }


    @Test
    public void testFeeds() throws Exception {
        put(new ApiTestRunnable<LoginTokenBean>() {

            @Override
            public void run() {
                getApiProvider().getUserApi().login("chenrui7", "chenrui123456789", null, this.listener());
            }
        });
        put(new ApiTestRunnable<UserFeedBean>() {
            @Override
            public void run() {
                mApi.getFeeds(1, "cs_net", this);
            }
        });

        runTestGroup();
    }

    @Test
    public void testFriendsInfo() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getFriendsInfo("gaochundong", listener(FriendsInfoBean.class));
            }
        });
    }

    @Test
    public void testFollow() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.follow("649b5d31-64f0-de11-ba8f-001cf0cd104b", listener(Void.class));
            }
        });
    }

    @Test
    public void testUnFollow() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.unFollow("649b5d31-64f0-de11-ba8f-001cf0cd104b", listener(Void.class));
            }
        });
    }

    @Test
    public void testBlogList() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getBlogList(12, "legendxian", listListener(BlogBean.class));
            }
        });
    }

    @Test
    public void testFollowList() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getFollowList(null, 1, listListener(UserInfoBean.class));
            }
        });
    }

    @Test
    public void testFansList() {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getFansList(null, 1, listListener(UserInfoBean.class));
            }
        });
    }
}
