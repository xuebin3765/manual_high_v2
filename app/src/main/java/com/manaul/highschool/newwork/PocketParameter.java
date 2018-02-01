package com.manaul.highschool.newwork;

import okhttp.PocketEncodingBuilder;
import okhttp3.RequestBody;

/**
 * 参数封装
 */
public class PocketParameter {
    private PocketEncodingBuilder mBuilder;

    public PocketParameter() {
        mBuilder = new PocketEncodingBuilder();
    }

    public PocketParameter add(String key, String value){
        mBuilder.add(key,value==null?"":value);
        return this;
    }

    public RequestBody getRequestBody(){
        return mBuilder.build();
    }

    /**
     * e.g key=value&key=value
     * @return key=value&key=value
     */
    @Override
    public String toString() {
        return mBuilder.toString();
    }


}
