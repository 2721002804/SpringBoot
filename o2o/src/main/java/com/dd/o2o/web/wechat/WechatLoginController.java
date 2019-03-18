package com.dd.o2o.web.wechat;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dd.o2o.dto.UserAccessToken;
import com.dd.o2o.dto.WechatAuthExecution;
import com.dd.o2o.dto.WechatUser;
import com.dd.o2o.entity.PersonInfo;
import com.dd.o2o.entity.WechatAuth;
import com.dd.o2o.enums.WechatAuthStateEnum;
import com.dd.o2o.service.PersonInfoService;
import com.dd.o2o.service.WechatAuthService;
import com.dd.o2o.util.wechat.WechatUtil;

@Controller
@RequestMapping("wechatlogin")
/**
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * 
 *
 */
public class WechatLoginController {

	private static Logger log = LoggerFactory.getLogger(WechatLoginController.class);
	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";
	@Autowired
	private PersonInfoService personInfoService;
	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		String code = request.getParameter("code");
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatUser user = null;
		String openId = null;
		WechatAuth auth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WechatUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WechatUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				auth = wechatAuthService.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		if (auth == null) {
			PersonInfo personInfo = WechatUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {
				personInfo.setUserType(1);
			} else {
				personInfo.setUserType(2);
			}
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.register(auth);
			if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				return null;
			} else {
				personInfo = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
				request.getSession().setAttribute("user", personInfo);
			}
		}
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";
		} else {
			return "shopadmin/shoplist";
		}
	}
}
