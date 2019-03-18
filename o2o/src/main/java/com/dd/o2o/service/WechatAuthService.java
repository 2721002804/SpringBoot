package com.dd.o2o.service;

import com.dd.o2o.dto.WechatAuthExecution;
import com.dd.o2o.entity.WechatAuth;
import com.dd.o2o.exceptions.WechatAuthOperationException;

public interface WechatAuthService {

	/**
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 
	 * @param wechatAuth
	 * @param profileImg
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException;

}
