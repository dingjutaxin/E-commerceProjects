package com.itmayiedu.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.fegin.MemberServiceFegin;
import com.itmayiedu.utils.CookieUtil;

import tk.mybatis.mapper.util.StringUtil;

@Controller
public class IndexController {
	private static final String INDEX = "index";
	private static final String LOGIN = "login";
	private static final String LOGOUT = "logout";
	
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	@RequestMapping("/logout")
	public String logout() {
		return LOGIN;
	}
	
	/**
	 * 	跳转主页
	 * @return
	 */
	@RequestMapping(value="/index",method = RequestMethod.GET)
	public String index(HttpServletRequest request) {
		//1.从cookie中获取token信息
		String token = CookieUtil.getUid(request, Constants.COOKIE_MEMBER_TOKEN);
		if(StringUtils.isEmpty(token)) {
			request.setAttribute("error", "token失效，重新登录！！");
			return LOGOUT;
		}
		//2.如果cooKie存在token，调用会员服务接口，使用token查询用户信息
		if(!StringUtil.isEmpty(token)) {
			ResponseBase responseBase = memberServiceFegin.fingUserByToken(token);
			if(responseBase.getCode().equals(Constants.HTTP_RES_CODE_200)) {
				LinkedHashMap userData= (LinkedHashMap) responseBase.getData();
				String username = (String) userData.get("username");
				request.setAttribute("username", username);
			}
		}
		return INDEX;
	}

}
