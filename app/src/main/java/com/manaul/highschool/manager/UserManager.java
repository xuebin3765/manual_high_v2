package com.manaul.highschool.manager;

import android.content.Context;

import com.manaul.highschool.bean.User;
import com.manaul.highschool.model.UserModel;
import com.manaul.highschool.utils.DataJson2ObjectUtil;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.SystemUtil;

/**
 * 用户信息管理者
 */
public class UserManager {

    public static UserModel getUser(Context context){
        SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(context);
        if(preferenceUtil.getLongByKey("userId") > 0 && preferenceUtil.getStringByKey("userAccessToken") != null){
            UserModel userModel = new UserModel();
            userModel.setId(preferenceUtil.getLongByKey("userId"));
            userModel.setAccount(preferenceUtil.getStringByKey("userAccount"));
            userModel.setNickName(preferenceUtil.getStringByKey("userNickName"));
            userModel.setRealName(preferenceUtil.getStringByKey("userRealName"));
            userModel.setCreated(preferenceUtil.getLongByKey("userCreated"));
            userModel.setAccessToken(preferenceUtil.getStringByKey("userAccessToken"));
            userModel.setPic(preferenceUtil.getStringByKey("userPic"));
            userModel.setGender(preferenceUtil.getIntByKey("userGender"));
            userModel.setPhone(preferenceUtil.getStringByKey("userPhone"));
            userModel.setEmail(preferenceUtil.getStringByKey("userEmail"));
            return userModel;
        }
        return null;

    }

    /**
     * 封装用户数据
     * @param context
     * @param json
     * @return
     */
    public static UserModel write2Pre(Context context , String json){
        UserModel userModel = DataJson2ObjectUtil.dataJson2Object(json , UserModel.class);
        if(userModel != null){
            SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(context);
            DebugUtil.d("user = "+userModel.toString());
            preferenceUtil.setLongByKey("userId" , userModel.getId());
            preferenceUtil.setStringByKey("userAccount" , userModel.getAccount());
            preferenceUtil.setStringByKey("userNickName" , userModel.getNickName());
            preferenceUtil.setStringByKey("userRealName" , userModel.getRealName());
            preferenceUtil.setLongByKey("userCreated" , userModel.getCreated());
            preferenceUtil.setStringByKey("userAccessToken" , userModel.getAccessToken());
            preferenceUtil.setStringByKey("userPic" , userModel.getPic());
            preferenceUtil.setIntByKey("userGender" , userModel.getGender());
            preferenceUtil.setStringByKey("userPhone" , userModel.getPhone());
            preferenceUtil.setStringByKey("userEmail" , userModel.getEmail());
        }
        return userModel;
    }

    /**
     * bmob 的用户存储
     * @param context
     * @param user
     * @return
     */
    public static User write2PreBmob(Context context , User user){
        if(user != null){
            SharedPreferenceUtil preferenceUtil = SharedPreferenceUtil.getInstance(context);
            DebugUtil.d("user = "+user.toString());
            preferenceUtil.setStringByKey("userObjectId" , user.getObjectId());
            preferenceUtil.setStringByKey("userAccount" , user.getUsername());
            preferenceUtil.setStringByKey("userNickName" , user.getNickName());
            preferenceUtil.setStringByKey("userCreatedAt" , user.getCreatedAt());
            preferenceUtil.setStringByKey("userAccessToken" , user.getAccessToken());
            preferenceUtil.setStringByKey("userHeadPic" , user.getHeadPic());
            preferenceUtil.setIntByKey("userGender" , user.getGender());
            preferenceUtil.setStringByKey("userEmail" , user.getEmail());
            preferenceUtil.setLongByKey("userEmail" , user.getVipEnd());
        }
        return user;
    }
}
