package com.itmayiedu.mq;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;
/**
 * 	生产者管理接口
 * @author Administrator
 *
 */
@Service
public class RegisterMailboxProducer {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	/**
	 * 	生产者推送消息 通过JmsMessagingTemplate
	 * @param destination	传队列
	 * @param json   		传消息
	 */
	public void sendMsg(Destination destination, String json) {
		jmsMessagingTemplate.convertAndSend(destination, json);
	}

}
