package com.itmayiedu.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.itmayiedu.constants.Constants;
import com.itmayiedu.email.service.EmailService;
import com.itmayiedu.adapter.MessageAdapter;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConsumerDistribute  {
	@Autowired
	private EmailService emailService;
	
	private MessageAdapter messageAdapter;
	/**
	 *	监听消息
	 *	监听"messages_queue"这个队列中的消息
	 *
	 *	mq推送的接口规范 ----通讯协议
	 *	内部mq协议使用json格式
	 *	{
	 *  	"header": {
	 *      	"interfaceType": "接口类型"
	 * 		},
	 * 		"content": {}
	 *	}
	 */
	@JmsListener(destination="messages_queue")
	public void distribute(String json) {
		log.info("###消息服务平台接收消息内容：{}###",json);
		if(StringUtils.isEmpty(json)) {
			return;
		}
		JSONObject rootJson= new JSONObject().parseObject(json);
		JSONObject header = rootJson.getJSONObject("header");
		String interfaceType = header.getString("interfaceType");
		if(StringUtils.isEmpty(interfaceType)) {
			return;
		}
		//判断接口类型是否为发邮件
		if(interfaceType.equals(Constants.MSG_EMAIL)) {
			//如果是发送邮件，就让发消息接口实现发邮件类
			messageAdapter = emailService;
		}
		if(messageAdapter == null) {
			return;
		}
		JSONObject contentJson = rootJson.getJSONObject("content");
		messageAdapter.sendMsg(contentJson);
	}

}
