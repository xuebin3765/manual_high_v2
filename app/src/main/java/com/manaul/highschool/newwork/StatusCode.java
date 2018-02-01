package com.manaul.highschool.newwork;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public interface StatusCode {
	/**
	 * 超时
	 */
	public static final int CODE_TIMEOUT = -4;

	/**
	 * 没有网络，网络连接失败
	 */
	public static final int CODE_CONNECT_ERROR = -3;

	/**
	 * 请求取消
	 */
	public static final int CODE_CANCEL = -2;

	/**
	 * 未知错误，一般为系统内部出错等
	 */
	public static final int CODE_UNKNOWN = -1;
	/**
	 * 失败
	 */
	public static final int CODE_ERROR = 0;
	/**
	 * 成功
	 */
	public static final int CODE_SUCCESS = 1;
	/**
	 * 请求参数有误
	 */
	public static final int CODE_PARAM_INCORRECT = 2;

	/**
	 * 记录找不到
	 */
	public static final int CODE_RECORD_NOT_FOUND = 3;

	/**
	 * 用户信息没有找到，有可能在bmob后台
	 */
	public static final int CODE_RECORD_NOT_FOUND_USER = 4;

	/**
	 * 还没有登录，需要登录
	 */
	public static final int CODE_NOT_LOGIN = 401;
	/**
	 * 没有权限
	 */
	public static final int CODE_DENIED = 403;
	/**
	 * 还没有登录，需要登录
	 */
	public static final int CODE_NOT_FOUND = 404;

	/**
	 * 服务器出错
	 */
	public static final int CODE_SERVER_ERROR = 500;

	/**
	 * 网络异常
	 */
	public static final int CODE_NETWORK_ERROR = 505;

}
