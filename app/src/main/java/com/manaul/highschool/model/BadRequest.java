package com.manaul.highschool.model;

import com.google.gson.Gson;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class BadRequest {
    //{"apiVersion":"1.0","message":"缺少channel参数","status":2,"total":0}
    private String apiVersion;
    private String message;
    private int status;

    public static BadRequest createInstanceByJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, BadRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
