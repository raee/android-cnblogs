package com.rae.cnblogs.sdk.bean;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ads")
public class AdvertBean extends Model {

    private String ad_id;

    @Column
    private String ad_end_date;
    @Column
    private String create_time;
    @Column
    private String ad_url;
    @Column
    private String image_url;
    @Column
    private String ad_name;
    @Column
    private String ad_type;
    @Column
    private String jump_type;

    public String getAd_end_date() {
        return this.ad_end_date;
    }

    public void setAd_end_date(String ad_end_date) {
        this.ad_end_date = ad_end_date;
    }

    public String getAd_id() {
        return this.ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAd_url() {
        return this.ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }


    public String getAd_name() {
        return this.ad_name;
    }

    public void setAd_name(String ad_name) {
        this.ad_name = ad_name;
    }

    public String getJump_type() {
        return this.jump_type;
    }

    public void setJump_type(String jump_type) {
        this.jump_type = jump_type;
    }

    public String getAd_type() {
        return ad_type;
    }

    public void setAd_type(String ad_type) {
        this.ad_type = ad_type;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
