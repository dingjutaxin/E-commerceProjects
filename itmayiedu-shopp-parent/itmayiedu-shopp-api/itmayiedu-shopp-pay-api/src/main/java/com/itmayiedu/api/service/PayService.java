package com.itmayiedu.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.entity.PaymentInfo;

@RequestMapping("/pay")
public interface PayService {
	/**
	 * 创建支付令牌token
	 * @param paymentInfo
	 * @return
	 */
	@RequestMapping("/createPayToken")
	public ResponseBase createPayToken(@RequestBody PaymentInfo paymentInfo);
	/**
	 * 使用支付令牌查找支付信息
	 * @param payToken
	 * @return
	 */
	@RequestMapping("/findPayToken")
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken);
	
	
}
