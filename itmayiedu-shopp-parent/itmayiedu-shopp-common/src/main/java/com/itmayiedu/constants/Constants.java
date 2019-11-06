package com.itmayiedu.constants;

import java.util.UUID;

/**
 * 常量类
 * @author Administrator
 */
public interface Constants {
	// 响应请求成功
	String HTTP_RES_CODE_200_VALUE = "success";
	// 系统错误
	String HTTP_RES_CODE_500_VALUE = "fial";
	// 响应请求成功code
	Integer HTTP_RES_CODE_200 = 200;
	// 系统错误
	Integer HTTP_RES_CODE_500 = 500;
	// 未关联qq账号
	Integer HTTP_RES_CODE_201 = 201;
	// Email
	String MSG_EMAIL = "email";
	// 会员token
	String TOKEN_MEMBER ="token_member";
	// 支付token
	String TOKEN_PAY = "token_pay";
	//支付宝异步回调失败
	String PAY_FAIL = "fail";
	//支付宝异步回调成功
	String PAY_SUCCESS = "success";
	// 用户有效期 90天
	Long TOKEN_MEMBER_TIME =(long) (60*60*24*90);
	int COOKIE_MEMBER_TIME =60*60*24*90;
	//支付token保存时间15分钟
	Long PAY_TOKEN_MEMBER_TIME =(long)  (60 * 15 );
	// cookie用户token名称
	String COOKIE_MEMBER_TOKEN = "coolie_member_token";


}
