package com.itmayiedu.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.entity.UserEntity;
import com.itmayiedu.fegin.MemberServiceFegin;
import com.itmayiedu.utils.CookieUtil;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.oauth.Oauth;

@Controller
public class LoginController {
	
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	private static final String INDEX = "redirect:index";
	private static final String  LOGIN= "login";
	private static final String  qqrelation = "qqrelation";
	private static final String  ERROR= "error";
	
	/**
	 * 跳转登录页面
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return LOGIN;
	}
	/**
	 * 登录业务具体实现
	 * @param userEntity
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public String loginPost(UserEntity userEntity,HttpServletRequest request,HttpServletResponse response){
		//1.调用登录接口
		ResponseBase login = memberServiceFegin.login(userEntity);
		if(!login.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "用户名或密码错误！！");
			return LOGIN;
		}
		//2.获取token信息
		LinkedHashMap linkedHashMap = (LinkedHashMap) login.getData();
		String token = (String)linkedHashMap.get("token");
		if(StringUtils.isEmpty(token)) {
			request.setAttribute("error", "token失效！！");
			return LOGIN;
		}
		//3.将token放进cookie中
		setCookie(token, response);
		return INDEX;
	}

	public void setCookie(String token, HttpServletResponse response){
		CookieUtil.addCookie(response, Constants.COOKIE_MEMBER_TOKEN, token, Constants.COOKIE_MEMBER_TIME);
	}
	 /**
     * 跳转到QQ授权地址
     * @param request
     * @return
     * @throws QQConnectException
     */
	@RequestMapping("/locaQQLogin")
	public String locaQQLogin(HttpServletRequest request) throws QQConnectException {
		//生成授权链接
		String authorizeURL = new Oauth().getAuthorizeURL(request);
		return "redirect:"+authorizeURL;
	}
	
	@RequestMapping("/qqLoginCallback")
	public String qqLoginCallback(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession)throws QQConnectException {
		
		//2.使用授权码code获取accessToken
		AccessToken accessTokenObj = new Oauth().getAccessTokenByRequest(request);
		if (accessTokenObj == null) {
			request.setAttribute("error", "qq授权失败!");
			return ERROR;
		}
		String accessToken = accessTokenObj.getAccessToken();
		if (StringUtils.isEmpty(accessToken)) {
			request.setAttribute("error", "qq授权失败!");
			return ERROR;
		}
		// 3.使用accessToken获取openid
		OpenID openIdObj = new OpenID(accessToken);
		String userOpenID = openIdObj.getUserOpenID();
		// 4.调用会员服务接口，使用userOpenid 查找是否已经关联账号
		ResponseBase openIdUser = memberServiceFegin.findUserByOpenid(userOpenID);
		// 5.用戶沒有关联QQ账号
		if (openIdUser.getCode().equals(Constants.HTTP_RES_CODE_201)) {
			// 跳转到管理账号
			httpSession.setAttribute("qqOpenid", userOpenID);
			return qqrelation;
		}
		// 6.如果用户关联账号 直接登录
		LinkedHashMap dataMap = (LinkedHashMap) openIdUser.getData();
		String token = (String) dataMap.get("token");
		setCookie(token, response);
		return INDEX;
	}
	
	// 登录请求具体提交实现
		@PostMapping(value = "/qqRelation")
		public String qqRelation(UserEntity userEntity, HttpServletRequest request, HttpServletResponse response,HttpSession httpSession) {
			// 1.获取openid
			String qqOpenid=(String) httpSession.getAttribute("qqOpenid");
			if(StringUtils.isEmpty(qqOpenid)){
				request.setAttribute("error", "没有获取到openid");
				return "error";
			}
			// 2.调用登录接口，获取token信息
			userEntity.setOpenid(qqOpenid);
			ResponseBase loginBase = memberServiceFegin.qqLogin(userEntity);
			if (!loginBase.getCode().equals(Constants.HTTP_RES_CODE_200)) {
				request.setAttribute("error", "账号或者密码错误!");
				return LOGIN;
			}

			LinkedHashMap loginData = (LinkedHashMap) loginBase.getData();
			String memberToken = (String) loginData.get("memberToken");
			if (StringUtils.isEmpty(memberToken)) {
				request.setAttribute("error", "会话已经失效!");
				return LOGIN;
			}
			// 3.将token信息存放在cookie里面
			setCookie(memberToken, response);
			return INDEX;
		}



}
