package com.itmayiedu.api.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itmayiedu.base.ResponseBase;

@RequestMapping("/callBack")
public interface PayCallBackService {
	
	//同步回调
	@RequestMapping("/synCallBack")
	public ResponseBase synCallBack(@RequestParam Map<String,String> params);

	//异步回调
	@RequestMapping("/asynCallBack")
	public String asynCallBack(@RequestParam Map<String,String> params);
}
