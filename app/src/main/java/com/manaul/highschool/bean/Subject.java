package com.manaul.highschool.bean;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class Subject {

    //主题名
    private String name;
    //主题图标资源ID
    private int icon;

    public Subject(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
