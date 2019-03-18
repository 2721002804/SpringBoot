package com.dd.o2o.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4970914046519819619L;
	@JsonProperty("openid")
	private String openId;
	// 用户昵称
	@JsonProperty("nickname")
	private String nickName;
	@JsonProperty("sex")
	private int sex;
	// 省份
	@JsonProperty("province")
	private String province;
	@JsonProperty("city")
	private String city;
	// 区
	@JsonProperty("country")
	private String country;
	@JsonProperty("headimgurl")
	private String headimgurl;
	@JsonProperty("language")
	private String language;
	// 用户权限
	@JsonProperty("privilege")
	private String[] privilege;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String[] getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String[] privilege) {
		this.privilege = privilege;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
