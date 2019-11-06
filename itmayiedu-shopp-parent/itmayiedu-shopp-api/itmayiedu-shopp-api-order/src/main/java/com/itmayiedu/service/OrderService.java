package com.itmayiedu.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itmayiedu.base.ResponseBase;

@RequestMapping("/order")
public interface OrderService {
	/**
	 * 更新订单
	 * @param isPay
	 * @param aliPayId
	 * @param orderNumber
	 * @return
	 */
	@RequestMapping("/updateOrderId")
	public ResponseBase updateOrderId(@RequestParam("isPay") Integer isPay, 
			@RequestParam("payId") String aliPayId,
			@RequestParam("orderNumber") String orderNumber);
}
