package com.itmayiedu.fegin;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import com.itmayiedu.api.service.PayCallBackService;

@Component
@FeignClient(value="pay")
public interface CallBackServiceFegin extends PayCallBackService{

}
