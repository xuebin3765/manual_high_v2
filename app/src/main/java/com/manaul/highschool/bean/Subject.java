package com.manaul.highschool.bean;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class Subject {

    //主题名
    private String name;
    //主题图标资源ID
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Subject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
