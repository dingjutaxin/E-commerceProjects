package com.itmayiedu.email.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.itmayiedu.adapter.MessageAdapter;

import lombok.extern.slf4j.Slf4j;
/**
 * 	处理第三方发送邮件
 */
@Slf4j
@Service
public class EmailService implements MessageAdapter {
	@Value("${msg.subject}")
	private String subject;
	@Value("${msg.text}")
	private String text;
	@Autowired
	private JavaMailSender javaMailSender;
	
	
	@Override
	public void sendMsg(JSONObject body) {
		// 处理发送邮件
		String email = body.getString("email");
		if(StringUtils.isEmpty(email)) {
			return;
		}
		log.info("####消息服务平台调用第三方接口发邮件：{}####",email);
		
		//对简单邮件进行建模
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		//发送邮件的账号，自己发自己
		simpleMailMessage.setFrom(email);
		//接收邮件的账号
		simpleMailMessage.setTo(email);
		//邮件标题
		simpleMailMessage.setSubject(subject);
		//邮件内容
		simpleMailMessage.setText(text.replace("{}", email));
		//发送邮件
		javaMailSender.send(simpleMailMessage);
		log.info("###消息服务平台发送邮件：{} 完成##",email);
		
	}

}
