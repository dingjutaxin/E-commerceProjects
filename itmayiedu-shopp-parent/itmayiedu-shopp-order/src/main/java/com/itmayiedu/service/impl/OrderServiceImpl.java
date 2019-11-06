package com.itmayiedu.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.itmayiedu.base.BaseApiService;
import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.dao.OrderDao;
import com.itmayiedu.entity.OrderEntity;
import com.itmayiedu.service.OrderService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
public class OrderServiceImpl extends BaseApiService implements OrderService {
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public ResponseBase updateOrderId(Integer isPay, String aliPayId, String orderNumber) {
		log.info("###OrderServiceImpl--updateOrderId开始执行#入参#isPay{}###",isPay);
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setIsPay(isPay);
		orderEntity.setPayId(aliPayId);
		orderEntity.setOrderNumber(orderNumber);
		orderEntity.setUpdated(new Date());
		int updateOrder = orderDao.updateOrder(orderEntity);
		if (updateOrder <= 0) {
			return setResultError("系统错误!");
		}
		log.info("###OrderServiceImpl--updateOrderId执行结束###");
		return setResultSuccess();
	}

}
