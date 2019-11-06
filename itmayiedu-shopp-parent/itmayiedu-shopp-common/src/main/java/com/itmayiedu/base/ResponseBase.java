package com.itmayiedu.base;

import lombok.Getter;
import lombok.Setter;
/**
 * 封装的DTO对象
 * @author Administrator
 *
 */
@Getter
@Setter
public class ResponseBase {
	//响应code
	private Integer code;
	//消息内容
	private String msg;
	//返回data
	private Object data;
	
	public ResponseBase() {
		
	}

	public ResponseBase(Integer code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}


}
