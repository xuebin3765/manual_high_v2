package com.manaul.highschool.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2019/4/11 0011.
 */

public class ReadFileUtil {

    /**
     * 读取json文件
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static <T> T readJsonToObject(String jsonString, Class<T> clazz){
        if (StringUtils.isNotEmpty(jsonString)){
            return JSON.parseObject(jsonString, clazz);
        }else {
            return null;
        }
    }

    public static <T> List<T> readJsonToObjectList(String jsonString, Class<T> clazz){
        List<T> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(jsonString)){
            JSONArray jsonArray = JSON.parseArray(jsonString);
            if (jsonArray != null && jsonArray.size() > 0){
                for (int i = 0; i < jsonArray.size(); i++){
                    list.add(JSON.parseObject(jsonArray.getString(i), clazz));
                }
            }
        }
        return list;
    }
}
