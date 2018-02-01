package com.manaul.highschool.newwork;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public interface PocketListener {

    /**
     * Activity发出的请求在UIThread 回调，其他的异步响应，不是UIThread
     * 请求成功，确保了json 数据是需要显示的数据，需要处理的是使用Gson 解析json 的时候可能实现null。例如把一个"" 解析为int 类型字段
     * 保证：
     * 1，http status code: 200
     * 2，custom status code：成功
     * @param json 响应的json 数据
     */
    void onOK(final String json, final String message, final int requestCode);

    /**
     *  Activity发出的请求在UIThread 回调，其他的异步响应，不是UIThread
     *  处理出现错误的情况，不管是哪种错误
     *  主要有的错误：
     *  1，无法找到主机
     *  2，没有网络
     *  3，连接超时
     *  4，无法找到资源 -- 404
     *  5，认证失败 -- 还没有登录
     *  6，Bad Request -- 参数不够
     * @param errorCode
     * @param msg
     */
    void onError(final int errorCode, final String msg, final int requestCode);

}
