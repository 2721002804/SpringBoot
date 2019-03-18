package com.dd.o2o.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import com.dd.o2o.dto.WechatAuthExecution;
import com.dd.o2o.entity.PersonInfo;
import com.dd.o2o.entity.WechatAuth;
import com.dd.o2o.enums.WechatAuthStateEnum;
@RunWith(SpringRunner.class)
@SpringBootTest
public class WechatAuthServiceTest{
	@Autowired
	private WechatAuthService wechatAuthService;

	@Test
	public void testRegister() {
		WechatAuth wechatAuth = new WechatAuth();
		PersonInfo personInfo = new PersonInfo();
		String openId = "service试一试";
		personInfo.setCreateTime(new Date());
		personInfo.setName("测试一下");
		personInfo.setUserType(1);
		wechatAuth.setPersonInfo(personInfo);
		wechatAuth.setOpenId(openId);
		wechatAuth.setCreateTime(new Date());
		WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
		assertEquals(WechatAuthStateEnum.SUCCESS.getState(), wae.getState());
		// 通过openId找到新增的wechatauth
		wechatAuth = wechatAuthService.getWechatAuthByOpenId(openId);
		System.out.println(wechatAuth.getPersonInfo().getName());
	}
}
