package com.itmayiedu.api.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.config.AlipayConfig;
import com.itmayiedu.base.BaseApiService;
import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.dao.PaymentInfoDao;
import com.itmayiedu.entity.PaymentInfo;
import com.itmayiedu.utils.TokenUtils;

@RestController
public class PayServiceImpl extends BaseApiService implements PayService {
	/** 待支付 */
	private static final Integer STATE_WAIT = 0;
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	
	/**
	 * 创建支付令牌
	 * @return
	 */
	@RequestMapping("/createPayToken")
	public ResponseBase createPayToken(@RequestBody PaymentInfo paymentInfo) {
		//0.验证订单是否存
		int countPayment = paymentInfoDao.countPaymentByOrderId(paymentInfo.getOrderId());
		if(countPayment > 0) {
			return setResultError("订单已经存在");
		}
		//1.创建支付请求信息
		paymentInfo.setState(STATE_WAIT);
		paymentInfo.setCreated(new Date());
		paymentInfo.setUpdated(paymentInfo.getCreated());
		Integer savePaymentType = paymentInfoDao.savePaymentType(paymentInfo);
		if(savePaymentType<=0) {
			return setResultError("创建支付订单失败！！");
		}
		//2.生成对应token
		String payToken = TokenUtils.getPayToken();
		//3.存放在Redis中，key为token value为支付id
		baseRedisService.setString(payToken, paymentInfo.getId()+"",Constants.PAY_TOKEN_MEMBER_TIME);
		//返回token
		JSONObject data = new JSONObject();
		data.put("payToken", payToken);
		return setResultSuccess("成功！！", data);
	}

	@Override
	@RequestMapping("/findPayToken")
	public ResponseBase findPayToken(@RequestParam("payToken") String payToken) {
		//1.参数校验
		if(StringUtils.isEmpty(payToken)) {
			return setResultError("token不能为空！");
		}
		//2.判断token有效
		//3.使用token查询Redis，找到对应支付id
		String payId = (String) baseRedisService.getString(payToken);
		if(StringUtils.isEmpty(payId)) {
			return setResultError("支付请求超时！");
		}
		//4.使用支付id，进行下单
		long payIdLong = Long.parseLong(payId);
		//5.使用支付id查询支付信息
		PaymentInfo paymentInfo = paymentInfoDao.getPaymentInfo(payIdLong);
		if(paymentInfo == null) {
			return setResultError("未找到支付信息！");
		}
		//6.对接支付代码，返回提交支付from表单给客户端，获得初始化的AlipayClient
		//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, "json", AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
		
		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig.return_url);
		alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		
		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no =paymentInfo.getOrderId();
		//付款金额，必填
		String total_amount = paymentInfo.getPrice()+"";
		//订单名称，必填
		String subject ="TextDemo";
		//商品描述，可空
//		String body =;
		
		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
				+ "\"total_amount\":\""+ total_amount +"\"," 
				+ "\"subject\":\""+ subject +"\"," 
//				+ "\"body\":\""+ body +"\"," 
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		
		//若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
		//alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"," 
		//		+ "\"total_amount\":\""+ total_amount +"\"," 
		//		+ "\"subject\":\""+ subject +"\"," 
		//		+ "\"body\":\""+ body +"\"," 
		//		+ "\"timeout_express\":\"10m\"," 
		//		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
		//请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
		
		//请求
		try {
			String result = alipayClient.pageExecute(alipayRequest).getBody();
			JSONObject data = new JSONObject();
			data.put("payHtml", result);
			return setResultSuccess("成功！！", data);
		} catch (Exception e) {
			// TODO: handle exception
			return setResultError("支付异常！");
		}
	}
}
