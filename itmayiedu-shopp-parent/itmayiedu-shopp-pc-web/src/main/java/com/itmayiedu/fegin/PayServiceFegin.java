package com.itmayiedu.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.itmayiedu.api.service.PayService;

@Component
@FeignClient(value="pay")
public interface PayServiceFegin extends PayService {

}
