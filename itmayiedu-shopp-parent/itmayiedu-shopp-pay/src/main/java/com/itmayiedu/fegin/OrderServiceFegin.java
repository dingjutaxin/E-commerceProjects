package com.itmayiedu.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.itmayiedu.service.OrderService;

@Component
@FeignClient(value="order")
public interface OrderServiceFegin extends OrderService{

}
