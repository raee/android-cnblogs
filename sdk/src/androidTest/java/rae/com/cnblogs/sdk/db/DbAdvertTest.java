package rae.com.cnblogs.sdk.db;

import android.util.Log;

import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.ICategoryApi;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.cnblogs.sdk.db.DbAdvert;
import com.rae.cnblogs.sdk.db.DbCategory;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rae.com.cnblogs.sdk.BaseTest;

/**
 * 广告测试
 * Created by ChenRui on 2017/1/25 0025 14:47.
 */
public class DbAdvertTest extends BaseTest {

    private DbAdvert mAdvert;

    @Override
    public void setup() {
        super.setup();
        mAdvert = new DbAdvert();
    }

    @Test
    public void testAdd() throws InterruptedException {
        AdvertBean data = new AdvertBean();
        data.setAd_id("1");
        data.setAd_name("test");
        data.setAd_type("CNBLOG_LAUNCHERa");
        data.setJump_type("URL");
        data.setImage_url("http://test.com");
        mAdvert.save(data);
        AdvertBean ad = mAdvert.getLauncherAd();

        Log.d("rae", "测试完成！" + (ad == null ? "NULL" : ad.getAd_name()));
    }


    @Test
    public void testAddCategory() {
        Category category = new Category();
        category.setName("test");
        category.save();

    }


    @Test
    public void testSaveCategory() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        ICategoryApi categoryApi = CnblogsApiFactory.getInstance(mContext).getCategoryApi();
        categoryApi.getCategory(new ApiUiArrayListener<Category>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                countDownLatch.countDown();
            }

            @Override
            public void onApiSuccess(List<Category> data) {
                DbCategory db = new DbCategory();
                db.reset(data);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await(10, TimeUnit.MINUTES);
    }

    @Test
    public void testFindCategory() {
        List<Category> list = new DbCategory().list();
        for (Category category : list) {
            Log.d("RAe", category.getCategoryId() + " = " + category.getName());
        }
    }

}
