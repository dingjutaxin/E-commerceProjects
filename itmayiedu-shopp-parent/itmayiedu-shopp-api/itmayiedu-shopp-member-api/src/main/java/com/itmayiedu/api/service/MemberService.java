package com.itmayiedu.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.entity.UserEntity;

@RequestMapping("/member")
public interface MemberService {
	
	/**
	 * 	基于用户id查询用户信息
	 * @param userId
	 * @return
	 */
	@RequestMapping("/findUserById")
	public ResponseBase findUserById(Long userId);
	/**
	 * 	用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping("/register")
	public ResponseBase register(@RequestBody UserEntity user);
	/**
	 * 用户登录
	 * @param usser
	 * @return
	 */
	@RequestMapping("/login")
	public ResponseBase login(@RequestBody UserEntity user);
	
	/**
	 * 	根据token获取用户信息,实现登录
	 * @param token
	 * @return
	 */
	@RequestMapping("/fingUserByToken")
	public ResponseBase fingUserByToken(@RequestParam("token")String token);
	/**
	 * 用户登录
	 * @param usser
	 * @return
	 */
	@RequestMapping("/qqLogin")
	public ResponseBase qqLogin(@RequestBody UserEntity user);
	/**
	 * 	根据openid获取用户信息,实现登录
	 * @param openid
	 * @return
	 */
	@RequestMapping("/findUserByOpenid")
	public ResponseBase findUserByOpenid(@RequestParam("openid")String openid);

}
