package com.itmayiedu.controller;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.prefs.BackingStoreException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.entity.PaymentInfo;
import com.itmayiedu.fegin.PayServiceFegin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class PayController {
	
	@Autowired
	private PayServiceFegin payServiceFegin;
	
	@PostMapping("/orderPay")
	public void orderPay(PaymentInfo paymentInfo,HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		//1.参数校验
		if(StringUtils.isEmpty(paymentInfo.getOrderId())) {
			return;
		}
		if(paymentInfo.getPrice() == null || paymentInfo.getPrice() <= 0) {
			return;
		}
		//2.调用生成支付令牌接口
		ResponseBase createPayToken = payServiceFegin.createPayToken(paymentInfo);
		if(!createPayToken.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			String msgString = createPayToken.getMsg();
			writer.println(msgString);
			return;
		}
		LinkedHashMap date =  (LinkedHashMap) createPayToken.getData();
		String payToken = (String) date.get("payToken");
		log.info("####PayController###payToken:{}", payToken);
		
		aliPay(payToken, response);
	}
	
	
	
	
	/**
	 * 使用token进行支付
	 * @param payToken
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("/aliPay")
	public void aliPay(String payToken,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		//1.参数校验
		if(StringUtils.isEmpty(payToken)) {
			return;
		}
		//2.调用支付服务接口，获取支付宝html元素
		ResponseBase payTokenResult = payServiceFegin.findPayToken(payToken);
		if(!payTokenResult.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			String msg = payTokenResult.getMsg();
			writer.println(msg);
			return;
		}
		//3.返回可以执行的html元素客户端
		LinkedHashMap data = (LinkedHashMap) payTokenResult.getData();
		String payHtml = (String) data.get("payHtml");
		log.info("####PayController###payHtml:{}", payHtml);
		//4.页面上进行渲染
		writer.println(payHtml);
		writer.close();
	}
}
