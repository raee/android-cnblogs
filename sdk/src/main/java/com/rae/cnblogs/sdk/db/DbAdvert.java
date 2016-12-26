package com.rae.cnblogs.sdk.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.rae.cnblogs.sdk.bean.AdvertBean;

/**
 * 广告表
 * Created by ChenRui on 2016/12/22 23:12.
 */
public class DbAdvert extends DbCnblogs {

    private final String mTbName = "ads";

    public DbAdvert(Context context) {
        super(context);
    }

    public AdvertBean getLauncherAd() {
        Cursor cursor = db().rawQuery("select * from ads where ad_type='CNBLOG_LAUNCHER'", null);
        if (cursor.moveToNext()) {
            AdvertBean m = new AdvertBean();
            m.setAd_id(readString(cursor, "ad_id"));
            m.setAd_name(readString(cursor, "ad_name"));
            m.setAd_type(readString(cursor, "ad_type"));
            m.setJump_type(readString(cursor, "jump_type"));
            m.setAd_url(readString(cursor, "ad_url"));
            m.setImage_url(readString(cursor, "image_url"));
            m.setAd_end_date(readString(cursor, "ad_end_date"));
            cursor.close();
            return m;
        }
        return null;
    }


    public void save(AdvertBean data) {
        // delete
        db().delete(mTbName, "ad_id=?", new String[]{data.getAd_id()});

        ContentValues values = new ContentValues();
        values.put("ad_id", data.getAd_id());
        values.put("ad_name", data.getAd_name());
        values.put("ad_type", data.getAd_type());
        values.put("jump_type", data.getJump_type());
        values.put("ad_url", data.getAd_url());
        values.put("image_url", data.getImage_url());
        values.put("ad_end_date", data.getAd_end_date());
        db().insert(mTbName, null, values);
    }
}
