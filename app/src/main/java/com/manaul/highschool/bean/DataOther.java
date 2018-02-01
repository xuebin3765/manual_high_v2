package com.manaul.highschool.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/1/31.
 */

public class DataOther extends BmobObject {
    private String homeTitle;
    private String zfbRedText;
    private String zfbRedUrl;

    public String getZfbRedText() {
        return zfbRedText;
    }

    public void setZfbRedText(String zfbRedText) {
        this.zfbRedText = zfbRedText;
    }

    public String getZfbRedUrl() {
        return zfbRedUrl;
    }

    public void setZfbRedUrl(String zfbRedUrl) {
        this.zfbRedUrl = zfbRedUrl;
    }

    public String getHomeTitle() {
        return homeTitle;
    }

    public void setHomeTitle(String homeTitle) {
        this.homeTitle = homeTitle;
    }
}
