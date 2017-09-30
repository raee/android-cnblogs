package rae.com.cnblogs.sdk;

import com.github.raee.runit.AndroidRUnit4ClassRunner;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IMomentApi;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * 闪存接口测试
 * Created by ChenRui on 2016/11/30 00:15.
 */
@RunWith(AndroidRUnit4ClassRunner.class)
public class MomentApiTest extends BaseTest {

    private IMomentApi mApi;

    @Override
    public void setup() {
        super.setup();
        mApi = CnblogsApiFactory.getInstance(mContext).getMomentApi();
    }

    @Test
    public void testPublish() throws InterruptedException {
        runTest("testPublish", mApi.publish("继续努力，加油！", 0));
    }

    @Test
    public void testMoments() throws InterruptedException {
        runTest("testMoments", mApi.getMoments(IMomentApi.MOMENT_TYPE_FOLLOWING, 1, System.currentTimeMillis()));
    }

}
