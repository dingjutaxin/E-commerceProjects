package com.itmayiedu.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.itmayiedu.constants.Constants;

/**
 * 封装的vo对象
 * @author Administrator
 *
 */
public class BaseApiService {
	@Autowired
	protected BaseRedisService baseRedisService;
	/**
	 * 	返回失败
	 * @param msg 提示信息
	 * @return
	 */
	public ResponseBase setResultError(Integer code,String msg) {
		return setResult(code,msg,null);
	}
	/**
	 * 	返回失败
	 * @param msg 提示信息
	 * @return
	 */
	public ResponseBase setResultError(String msg) {
		return setResult(Constants.HTTP_RES_CODE_500,msg,null);
	}
	/**
	 * 	返回成功
	 * @param msg 提示信息
	 * @return
	 */
	public ResponseBase setResultSuccess(String msg) {
		return setResult(Constants.HTTP_RES_CODE_200,msg,null);
	}
	/**
	 *	返回成功
	 * @return
	 */
	public ResponseBase setResultSuccess() {
		return setResult(Constants.HTTP_RES_CODE_200,Constants.HTTP_RES_CODE_200_VALUE,null);
	}
	//返回成功，没有data值
	/**
	 *	返回成功
	 * @param msg 提示信息
	 * @param data 结果数据
	 * @return
	 */
	public ResponseBase setResultSuccess(String msg,Object data) {
		return setResult(Constants.HTTP_RES_CODE_200,msg,data);
	}
	//通用封装
	public ResponseBase setResult(Integer code,String msg,Object data) {
		return new ResponseBase(code,msg,data);
	}

}
