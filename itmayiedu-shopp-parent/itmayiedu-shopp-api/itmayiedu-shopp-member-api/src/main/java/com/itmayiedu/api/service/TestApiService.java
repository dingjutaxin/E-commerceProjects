package com.itmayiedu.api.service;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itmayiedu.base.ResponseBase;
import com.itmayiedu.entity.UserEntity;

@RequestMapping("/member")
public interface TestApiService {
	@RequestMapping("/test")
	public Map<String,Object> test(Integer id,String name);
	
	@RequestMapping("/testResponseBase")
	public ResponseBase testResponseBase(Integer id,String name);
	
	@RequestMapping("/testSetRedis")
	public ResponseBase testSetRedis(String key,String value);
	
	

}
