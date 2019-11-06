package com.itmayiedu.api.setvice.impl;

import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.itmayiedu.api.service.MemberService;
import com.itmayiedu.base.BaseApiService;
import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.dao.UserDao;
import com.itmayiedu.entity.UserEntity;
import com.itmayiedu.mq.RegisterMailboxProducer;
import com.itmayiedu.utils.MD5Util;
import com.itmayiedu.utils.TokenUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService {
	@Value("${messages.queue}")
	private String MESSAGESQUEUE;
	@Autowired
	private RegisterMailboxProducer registerMailboxProducer;
	
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public ResponseBase findUserById(Long userId) {
		UserEntity user = userDao.findByID(userId);
		if(user == null) {
			return setResultError("未查找到该用户信息");
		}
		return setResultSuccess(null,user);
	}

	@Override
	public ResponseBase register(@RequestBody UserEntity user) {
		//参数校验
		if(StringUtils.isEmpty(user.getEmail())){
			return setResultError("邮箱不能为空！");
		}
		if(StringUtils.isEmpty(user.getUsername())){
			return setResultError("用户名不能为空！");
		}
		if(StringUtils.isEmpty(user.getPassword())){
			return setResultError("密码不能为空！");
		}
//		if(StringUtils.isEmpty(user.getPhone())){
//			return setResultError("手机号不能为空！");
//		}
		//加密
		user.setPassword(MD5Util.MD5(user.getPassword()));
		//时间
		user.setCreated(new Date());
		user.setUpdated(user.getCreated());
		//保存数据
		Integer insertUser = userDao.insertUser(user);
		if(insertUser <= 0) {
			return setResultError("注册失败！！");
		}
		//采用异步的方式发送消息
		String json = emailJson(user.getEmail());
		sendMsg(json);
		log.info("####会员服务推送消息到消息服务平台####json:{}",json);
		return setResultSuccess("注册成功！！");
	}
	/**
	 * 	封装json串
	 * 	格式：
	 * {
	 *  	"header": {
	 *      	"interfaceType": "接口类型"
	 * 		},
	 * 		"content": {
	 * 			"email":"email"
	 * 		}
	 *	}
	 */
	public String emailJson(String email) {
		JSONObject rootJson = new JSONObject();
		JSONObject header = new JSONObject();
		header.put("interfaceType", Constants.MSG_EMAIL);
		JSONObject content = new JSONObject();
		content.put("email", email);
		rootJson.put("header", header);
		rootJson.put("content", content);
		return rootJson.toJSONString();
	}
	/**
	 * 将分装的json串放入到消息队列中去
	 * @param json
	 */
	public void sendMsg(String json) {
		ActiveMQQueue activeMQQueue= new ActiveMQQueue(MESSAGESQUEUE);
		registerMailboxProducer.sendMsg(activeMQQueue, json);
		log.info("##发送的消息内容：{}##",json);
	}

	@Override
	public ResponseBase login(@RequestBody UserEntity user) {
		String username = user.getUsername();
		String password = user.getPassword();
		if(StringUtils.isEmpty(username)) {
			return setResultError("用户名不能为空！");
		}
		if(StringUtils.isEmpty(password)) {
			return setResultError("密码不能为空！");
		}
		String newPassWord = MD5Util.MD5(password);
		UserEntity userEntity = userDao.login(username, newPassWord);
		return setLogin(userEntity);
	}
	//生成token
	private ResponseBase setLogin(UserEntity userEntity) {
		if (userEntity == null) {
			return setResultError("账号或密码错误!");
		}
		// 账号正确，生成对应token
		String token = TokenUtils.getMemberToken();
		// 存放在redis中，Kay为token，value为userid
		baseRedisService.setString(token, userEntity.getId()+"",Constants.TOKEN_MEMBER_TIME);
		// 直接返回token
		JSONObject JSONObject = new JSONObject();
		JSONObject.put("token", token);
		return setResultSuccess(null,JSONObject);
	}

	@Override
	public ResponseBase fingUserByToken(@RequestParam("token") String token) {
		if (StringUtils.isEmpty(token)) {
			return setResultError("token不能为空.");
		}
		String userId = (String) baseRedisService.getString(token);
		if(StringUtils.isEmpty(userId)){
			return setResultError("未查询到用户信息");
		}
		Long userIdl=Long.parseLong(userId);
		UserEntity userEntity = userDao.findByID(userIdl);
		if (userEntity == null) {
			return setResultError("未查询到用户信息");
		}
		userEntity.setPassword(null);
		return setResultSuccess(null,userEntity);

	}

	@Override
	public ResponseBase qqLogin(@RequestBody UserEntity user) {
		//1.验证参数
		String openid = user.getOpenid();
		String username = user.getUsername();
		String password = user.getPassword();
		if(StringUtils.isEmpty(openid)) {
			return setResultError("openid不能为空！");
		}
		if(StringUtils.isEmpty(username)) {
			return setResultError("用户名不能为空！");
		}
		if(StringUtils.isEmpty(password)) {
			return setResultError("密码不能为空！");
		}
		//2.先进行账号登录，
		String newPassWord = MD5Util.MD5(password);
		UserEntity userEntity = userDao.login(username, newPassWord);
		//失败，返回失败信息
		if (userEntity == null) {
			return setResultError("账号或密码错误!");
		}
		//3.如果登录成功，数据库修改对应的openid
		
		Integer userid = userEntity.getId();
		Integer updateByOpenIdUser = userDao.updateByOpenidUser(openid,userid);
		if(updateByOpenIdUser<=0) {
			return setResultError("qq账号关联失败！");
		}
		return setLogin(userEntity);
	}

	@Override
	public ResponseBase findUserByOpenid(@RequestParam("openid") String openid) {
		//1.验证参数
		if(StringUtils.isEmpty(openid)) {
			return setResultError("openid不能为空！");
		}
		//2.使用openid查询数据库user表对应数据信息
		UserEntity userEntity = userDao.findUserByOpenid(openid);
		if(userEntity == null) {
			return setResultError(Constants.HTTP_RES_CODE_201, "该openid没有关联！！");
		}
		//3.自动登录
		return setLogin(userEntity);
	}

}
