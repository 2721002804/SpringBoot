package com.dd.o2o.util.wechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dd.o2o.dto.UserAccessToken;
import com.dd.o2o.dto.WechatUser;
import com.dd.o2o.entity.PersonInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 公众平台通用接口工具类
 * 
 */
public class WechatUtil {
	private static Logger log = LoggerFactory.getLogger(WechatUtil.class);

	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		String appId = "wxeca617ee89ebfbcd";
		log.debug("appId:" + appId);
		String appsecret = "733549f87bff85aa1a7d8b25199f158a";
		log.debug("secret:" + appsecret);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appsecret
				+ "&code=" + code + "&grant_type=authorization_code";
		String tokenStr = httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + tokenStr);
		UserAccessToken token = new UserAccessToken();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			token = objectMapper.readValue(tokenStr, UserAccessToken.class);
		} catch (JsonParseException e) {
			log.error("获取用户accessToken失败：" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			log.error("获取用户accessToken失败：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("获取用户accessToken失败：" + e.getMessage());
			e.printStackTrace();
		}
		if (token == null) {
			log.error("获取用户accessToken失败。");
			return null;
		}
		return token;
	}

	public static WechatUser getUserInfo(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId
				+ "&lang=zh_CN";
		String userStr = httpsRequest(url, "GET", null);
		WechatUser user = new WechatUser();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			user = objectMapper.readValue(userStr, WechatUser.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			log.error("获取用户UserInfo失败：" + e.getMessage());
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			log.error("获取用户UserInfo失败：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("获取用户UserInfo失败：" + e.getMessage());
			e.printStackTrace();
		}
		String openid = user.getOpenId();
		if (openid == null) {
			log.debug("获取用户信息失败。");
			return null;
		}
		return user;
	}

	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
		StringBuffer buffer = new StringBuffer();
		try {
			// 创建SSLContext对象，并用我们指定的信任管理器初始化
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestMethod(requestMethod);
			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}
			// 当有数据需要提交时
			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}
			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			log.debug("https buffer:" + buffer.toString());
		} catch (ConnectException ce) {
			log.error("Wechat server connection timed out");
		} catch (Exception e) {
			log.error("https request error:{}", e);
		}
		return buffer.toString();
	}

	public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex() + "");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}
}