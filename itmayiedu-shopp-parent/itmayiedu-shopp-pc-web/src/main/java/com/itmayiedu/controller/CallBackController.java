package com.itmayiedu.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.fegin.CallBackServiceFegin;

import lombok.extern.slf4j.Slf4j;	

@Slf4j
@RequestMapping("/alibaba/callBack")
@Controller
public class CallBackController {
	@Autowired
	private CallBackServiceFegin callBackServiceFegin;
	
	// 错误页面
	private static final String ERROR = "error";
	private static final String PAY_SUCCESS = "pay_success";
	//同步回调
	@RequestMapping("/returnUrl")
	public void synCallBack(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用
			valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		ResponseBase synCallBack = callBackServiceFegin.synCallBack(params);
		if (!synCallBack.getCode().equals(Constants.HTTP_RES_CODE_200)) {
			// 报错页面
			return;
		}
		LinkedHashMap data = (LinkedHashMap) synCallBack.getData();
		
		log.info("###支付宝同步回调CallBackController####synCallBack結束 params:{}", params);
		
		String htmlFrom = "<form name='punchout_form'"
				+ " method='post' action='http://127.0.0.1/alibaba/callBack/synSuccessPage' >"
				+ "<input type='hidden' name='outTradeNo' value='" + data.get("outTradeNo") + "'>"
				+ "<input type='hidden' name='tradeNo' value='" + data.get("tradeNo") + "'>"
				+ "<input type='hidden' name='totalAmount' value='" + data.get("totalAmount") + "'>"
				+ "<input type='submit' value='立即支付' style='display:none'>"
				+ "</form><script>document.forms[0].submit();" + "</script>";
		writer.println(htmlFrom);
		writer.close();
	}

	// 同步回调,解决隐藏参数
	@PostMapping("/synSuccessPage")
	public String synSuccessPage(HttpServletRequest request, String outTradeNo, String tradeNo, String totalAmount) {
		request.setAttribute("outTradeNo", outTradeNo);
		request.setAttribute("tradeNo", tradeNo);
		request.setAttribute("totalAmount", totalAmount);
		return PAY_SUCCESS;
	}

	// 异步回调
	@ResponseBody
	@RequestMapping("/notifyUrl")
	public String asynCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, String[]> requestParams = request.getParameterMap();
		Map<String, String> params = new HashMap<String, String>();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		log.info("###支付宝异步回调CallBackController####notifyUrl开始 params:{}", params);
		return callBackServiceFegin.asynCallBack(params);
	}
}
