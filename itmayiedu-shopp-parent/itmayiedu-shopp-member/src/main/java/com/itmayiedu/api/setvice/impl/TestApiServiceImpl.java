package com.itmayiedu.api.setvice.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RestController;

import com.itmayiedu.api.service.TestApiService;
import com.itmayiedu.base.BaseApiService;
import com.itmayiedu.base.ResponseBase;


@RestController
public class TestApiServiceImpl extends BaseApiService implements TestApiService {
	
	
	@Override
	public Map<String, Object> test(Integer id, String name) {
		Map<String,Object> result = new HashMap<>();
		result.put("rtnCode", "200");
		result.put("rtnMsg", "success");
		result.put("date", id+":"+name);
		return result;
	}

	@Override
	public ResponseBase testResponseBase(Integer id, String name) {
		return setResultSuccess();
	}

	@Override
	public ResponseBase testSetRedis(String key, String value) {
		baseRedisService.setString(key, value, null);
		return setResultSuccess();
	}

	

}
