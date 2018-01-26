package com.manaul.highschool.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 将data中的数据转存为对象
 * Created by Administrator on 2018/1/24.
 */

public class DataJson2ObjectUtil {
    public static <T> T dataJson2Object( String json , Class<T> clazz){
        if(json == null || json.trim().length() == 0) return null;
        JSONObject object = JSON.parseObject(json);
        if(object == null || !object.containsKey("data")) return null;
        return JSON.parseObject(object.getString("data") , clazz);

    }

    public static <T> T strJson2Object( String json , Class<T> clazz){
        if(json == null || json.trim().length() == 0) return null;
        return JSON.parseObject(json , clazz);

    }

    public static List<?> dataJson2ObjectArray(String json , Class<?> clazz){
        List<Object> list = new ArrayList<>();
        if(json == null || json.trim().length() == 0)
            return null;
        JSONObject object = JSON.parseObject(json);

        if(object == null || !object.containsKey("data"))
            return null;
        JSONArray jsonArray = object.getJSONArray("data");
        if(jsonArray != null && jsonArray.size() > 0){
            for (int i = 0 ; i < jsonArray.size() ; i++){
                Object parseObject = JSON.parseObject(jsonArray.getString(i) , clazz);
                list.add(parseObject);
            }
        }
        return list;
    }
}
