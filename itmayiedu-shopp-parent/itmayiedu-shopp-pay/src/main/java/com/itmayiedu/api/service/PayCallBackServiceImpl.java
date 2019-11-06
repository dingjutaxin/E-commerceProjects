package com.itmayiedu.api.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.config.AlipayConfig;
import com.itmayiedu.base.BaseApiService;
import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.constants.Constants;
import com.itmayiedu.dao.PaymentInfoDao;
import com.itmayiedu.entity.PaymentInfo;
import com.itmayiedu.fegin.OrderServiceFegin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class PayCallBackServiceImpl extends BaseApiService implements PayCallBackService {
	/** 支付成功 */
	private static final Integer STATE_SUCCESS = 1;
	/** 支付失败 */
	private static final Integer STATE_ERROR = 2;
	
	@Autowired
	private PaymentInfoDao paymentInfoDao;
	@Autowired
	private OrderServiceFegin orderServiceFegin;
	//同步回调                
	@Override
	public ResponseBase synCallBack(@RequestParam Map<String, String> params) {
		try {
			log.info("###同步回调开始####params:{}",params);
			
			//调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); 
			
			if(!signVerified) {
				return setResultError("验签失败！");
			}
			//商户订单号
			String outTradeNo = params.get("out_trade_no");
			//支付宝交易号
			String tradeNo =params.get("trade_no");
			//付款金额
			String totalAmount = params.get("total_amount");
			
			JSONObject data = new JSONObject();
			data.put("outTradeNo", outTradeNo);
			data.put("tradeNo", tradeNo);
			data.put("totalAmount", totalAmount);
			return setResultSuccess("同步回调成功！", data);
		} catch (Exception e) {
			log.info("######PayCallBackServiceImpl##ERROR:#####", e);
			return setResultError("系统错误！");
		}finally {
			log.info("###同步回调结束####params:{}",params);
		}
	}

	//异步回调
	@Override
	public String asynCallBack(@RequestParam Map<String, String> params) {
		log.info("###异步回调开始####params:{}",params);
		try {
			//调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); 
			log.info("#####支付宝异步通知signVerified:{}######", signVerified);
			//——请在这里编写您的程序（以下代码仅作参考）——
			
			/* 实际验证过程建议商户务必添加以下校验：
			1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
			2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
			3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
			4、验证app_id是否为该商户本身。
			*/
			
			if(!signVerified) {//验签失败
				return Constants.PAY_FAIL;
			}
			//商户订单号
			String outTradeNo = params.get("out_trade_no");
			//支付宝交易号
			String tradeNo = params.get("trade_no");		
			//交易状态
			String tradeStatus = params.get("trade_status");
			
			if(tradeStatus.equals("TRADE_SUCCESS")){
				/*
				 * 判断该笔订单是否在商户网站中已经做过处理
				 * 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				 * 如果有做过处理，不执行商户的业务程序
				 * 付款完成后，支付宝系统发送该交易状态通知
				*/
				
				//处理幂等 查询支付订单
				PaymentInfo paymentInfo = paymentInfoDao.getByOrderIdPayInfo(outTradeNo);
				if(paymentInfo == null) {//订单不存在
					return Constants.PAY_FAIL;
				}
				// 判断是否已经支付过，如果已经支付过,返回success,防止重试产生重复
				Integer state = paymentInfo.getState();
				if(state == 1) {//交易完成
					return Constants.PAY_SUCCESS;
				}
				
				//更新支付订单
				paymentInfo.setPayMessage(params.toString());//支付报文
				paymentInfo.setPlatformorderId(tradeNo);//支付宝交易号
				paymentInfo.setState(STATE_SUCCESS);// 标识为已经支付
				paymentInfo.setUpdated(new Date());
				// 手动 begin 
				int updatePayResult = paymentInfoDao.updatePayInfo(paymentInfo);
				if(updatePayResult <= 0 ) {
					return Constants.PAY_FAIL;
				}
				// 调用订单接口通知 更新支付状态
				ResponseBase orderResult = orderServiceFegin.updateOrderId(STATE_SUCCESS, tradeNo, outTradeNo);
				if (!orderResult.getCode().equals(Constants.HTTP_RES_CODE_200)) {
					// 回滚 手动回滚 rollback
					return Constants.PAY_FAIL;
				} 
				//int i=1/0; //如果在这里出错了用 2PC 3PC TCC MQ 进行分布式事物提交 
				// 手动 提交 comiit;
				return Constants.PAY_SUCCESS;
			}
			
			return Constants.PAY_FAIL;
		} catch (Exception e) {
			log.info("######PayCallBackServiceImpl##ERROR:#####", e);
			// 回滚 手动回滚 rollback
			return Constants.PAY_FAIL;
		}finally {
			log.info("###异步回调结束####params:{}",params);
		}
	}

}
