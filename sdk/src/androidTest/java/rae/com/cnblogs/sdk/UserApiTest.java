package rae.com.cnblogs.sdk;

import android.support.test.runner.AndroidJUnit4;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IUserApi;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.impl.WebUserApiImpl;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 用户接口测试
 * Created by ChenRui on 2017/1/14 01:09.
 */
@RunWith(AndroidJUnit4.class)
public class UserApiTest extends BaseTest {
    IUserApi mApi;

    @Override
    @Before
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getUserApi(mContext);
    }

    @Test
    public void testLogin() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.login("chenrui7", "chenrui123456789", new ApiUiListener<LoginTokenBean>() {
                    @Override
                    public void onApiFailed(ApiException e, String s) {
                        error(s);
                    }

                    @Override
                    public void onApiSuccess(LoginTokenBean loginTokenBean) {
                        stop(loginTokenBean.getAccess_token());
                    }
                });
            }
        });
    }

    @Test
    public void testRefreshToken() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.refreshLoginToken(new ApiUiListener<LoginTokenBean>() {
                    @Override
                    public void onApiFailed(ApiException e, String s) {
                        error(s);
                    }

                    @Override
                    public void onApiSuccess(LoginTokenBean loginTokenBean) {
                        stop(loginTokenBean.getAccess_token());
                    }
                });
            }
        });
    }

    @Test
    public void testUserInfo() throws InterruptedException {
        startTest(new Runnable() {
            @Override
            public void run() {
                mApi.getUserInfo(new ApiUiListener<UserInfoBean>() {
                    @Override
                    public void onApiFailed(ApiException e, String s) {
                        error(s);
                    }

                    @Override
                    public void onApiSuccess(UserInfoBean userInfo) {
                        stop(userInfo.getDisplayName());
                    }
                });
            }
        });
    }

    @Test
    public void testWebApiLogin() {
        startTest(new Runnable() {
            @Override
            public void run() {
                new WebUserApiImpl(mContext).login("rae", "123123", listener(LoginTokenBean.class));
            }
        });
    }
}
