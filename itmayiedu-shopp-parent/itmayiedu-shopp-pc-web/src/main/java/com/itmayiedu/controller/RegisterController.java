package com.itmayiedu.controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.entity.UserEntity;
import com.itmayiedu.fegin.MemberServiceFegin;

@Controller
public class RegisterController {
	@Autowired
	private MemberServiceFegin memberServiceFegin;
	
	private static final String REGISTER = "register";
	private static final String  LOGIN= "login";
	
	/**
	 * 跳转注册页面
	 * @return
	 */
	@GetMapping("/register")
	public String register() {
		return REGISTER;
	}
	/**
	 * 注册业务具体实现
	 * @param userEntity
	 * @param request
	 * @return
	 */
	@PostMapping("/register")
	public String registerPost(UserEntity userEntity,HttpServletRequest request){
		//2.调用会员注册接口
		ResponseBase response = memberServiceFegin.register(userEntity);
		//3.失败，跳转失败页
		if(!response.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			request.setAttribute("error", "注册失败！");
			return REGISTER;
		}
		//4.成功，跳转成功页
		return LOGIN;
	}

}
