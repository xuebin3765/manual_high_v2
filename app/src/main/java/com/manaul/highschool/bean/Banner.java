package com.manaul.highschool.bean;

import cn.bmob.v3.BmobObject;

/**
 * 轮播图
 * Created by Administrator on 2017/11/22.
 */

public class Banner extends BmobObject {
    private String title;
    private String contentUrl ;
    private String imageUtl;
    private int sort;
    private int type; // 1：启动图，2：首页轮播图
    private int appId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUtl() {
        return imageUtl;
    }

    public void setImageUtl(String imageUtl) {
        this.imageUtl = imageUtl;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
}
