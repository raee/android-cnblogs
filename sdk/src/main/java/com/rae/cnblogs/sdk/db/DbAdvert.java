package com.rae.cnblogs.sdk.db;

import com.activeandroid.query.Select;
import com.rae.cnblogs.sdk.bean.AdvertBean;

/**
 * 广告表
 * Created by ChenRui on 2016/12/22 23:12.
 */
public class DbAdvert extends DbCnblogs<AdvertBean> {

    public DbAdvert() {
    }

    public AdvertBean getLauncherAd() {
        return new Select().from(AdvertBean.class).where("ad_type = ?", "CNBLOG_LAUNCHER").executeSingle();
    }


    public void save(AdvertBean data) {
        data.save();
    }

}
