package com.dd.o2o.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dd.o2o.dao.PersonInfoDao;
import com.dd.o2o.dao.WechatAuthDao;
import com.dd.o2o.dto.WechatAuthExecution;
import com.dd.o2o.entity.PersonInfo;
import com.dd.o2o.entity.WechatAuth;
import com.dd.o2o.enums.WechatAuthStateEnum;
import com.dd.o2o.exceptions.WechatAuthOperationException;
import com.dd.o2o.service.WechatAuthService;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {
	private static Logger log = LoggerFactory.getLogger(WechatAuthServiceImpl.class);
	@Autowired
	private WechatAuthDao wechatAuthDao;
	@Autowired
	private PersonInfoDao personInfoDao;

	@Override
	public WechatAuth getWechatAuthByOpenId(String openId) {
		// TODO Auto-generated method stub
		return wechatAuthDao.queryWechatInfoByOpenId(openId);
	}

	@Override
	@Transactional
	public WechatAuthExecution register(WechatAuth wechatAuth) throws WechatAuthOperationException {
		// TODO Auto-generated method stub
		if (wechatAuth == null || wechatAuth.getOpenId() == null) {
			return new WechatAuthExecution(WechatAuthStateEnum.NULL_AUTH_INFO);
		}
		try {
			// 设置创建时间
			wechatAuth.setCreateTime(new Date());
			// 如果第一次使用该平台，则自动创建
			if (wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId() == null) {
				try {
					wechatAuth.getPersonInfo().setCreateTime(new Date());
					wechatAuth.getPersonInfo().setEnableStatus(1);
					PersonInfo personInfo = wechatAuth.getPersonInfo();
					int effectedNum = personInfoDao.insertPersonInfo(personInfo);
					wechatAuth.setPersonInfo(personInfo);
					if (effectedNum <= 0) {
						throw new WechatAuthOperationException("添加用户信息失败");
					}
				} catch (Exception e) {
					log.error("insertPersonInfo error:" + e.toString());
					throw new WechatAuthOperationException("insertPersonInfo error:" + e.getMessage());
				}
			}
			// 创建专属于本平台的微信账号
			int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
			if (effectedNum <= 0) {
				throw new WechatAuthOperationException("账号创建失败");
			} else {
				return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS, wechatAuth);
			}
		} catch (Exception e) {
			log.error("insertWechatAuth error:" + e.toString());
			throw new WechatAuthOperationException("insertWechatAuth error:" + e.getMessage());
		}

	}

}
