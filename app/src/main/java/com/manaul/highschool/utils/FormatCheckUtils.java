package com.manaul.highschool.utils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/8/12.
 */

public class FormatCheckUtils {
    /**
     * 验证账户，手机号或邮箱
     *  现在只支持 邮箱 ， 修改密码需要邮箱
     * @param account
     * @return
     */
    public static boolean isAccount(String account){
        return /*isPhoneLegal(account) || isPhoneHK(account) || */isEmail(account);
    }

    /**
     * 验证大陆手机号
     * @return
     */
    public static boolean isPhoneLegal(String phone){
        String regExp = "^((13[0-9])|(15[0-9])|(18[0-9])|(17[0-9])|(147))\\d{8}$";
        return Pattern.matches(regExp , phone);
    }

    /**
     * 验证香港手机号
     * @return
     */
    public static boolean isPhoneHK(String phone){
        String rex = "^(5|6|8|9)\\\\d{7}$";
        return Pattern.matches(rex , phone);
    }

    /**
     * 验证邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        String rex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return Pattern.matches(rex , email);
    }

    /**
     * 验证密码 6--18为字符
     * @param password
     * @return
     */
    public static boolean isPassword(String password){
        String rex = "[a-zA-Z0-9_]{6,18}";
        return Pattern.matches(rex , password);
    }
}
