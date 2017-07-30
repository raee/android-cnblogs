package com.rae.cnblogs.sdk.db;

import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.AdvertBean;

/**
 * 广告表
 * Created by ChenRui on 2016/12/22 23:12.
 */
public class DbAdvert extends DbCnblogs {

    DbAdvert() {
    }

    protected From where(String clause, Object... args) {
        return new Select().from(AdvertBean.class).where(clause, args);
    }

    public AdvertBean getLauncherAd() {
        return where("ad_type = ?", "CNBLOG_LAUNCHER").orderBy("id desc").executeSingle();
    }

    private void delete(AdvertBean m) {
        new Delete().from(AdvertBean.class).where("ad_type = ? and ad_name=?", m.getAd_type(), m.getAd_name()).execute();
    }


    public void save(final AdvertBean data) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                executeTransaction(new Runnable() {
                    @Override
                    public void run() {
                        // 删除数据
                        delete(data);
                        data.save();
                    }
                });
            }
        }).start();
    }

    /**
     * 清除缓存
     */
    void clearCache() {
        executeTransaction(new Runnable() {
            @Override
            public void run() {

                new Delete().from(AdvertBean.class).execute();
            }
        });
    }
}
