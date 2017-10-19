package com.manaul.highschool.bean;

import java.io.Serializable;

/**
 * Created by lovebin on 2017/2/19.
 */
@SuppressWarnings("serial")
public class Project implements Serializable {

    private Integer projectId; //id
    private String imageUrl;  //  封面
    private int status;       //  状
    private String type;       //  科目类型
    private String intro;
    private String name;
    private int total;
    public int getTotal() {
        return this.total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getIntro() {
        return this.intro;
    }
    public void setIntro(String intro) {
        this.intro = intro;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getImageUrl() {
        return this.imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public Integer getProjectId() {
        return this.projectId;
    }
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    
    public Project(Integer projectId, String imageUrl, int status, String type,
            String intro, String name, int total) {
        this.projectId = projectId;
        this.imageUrl = imageUrl;
        this.status = status;
        this.type = type;
        this.intro = intro;
        this.name = name;
        this.total = total;
    }
    
    public Project() {
    }

}
