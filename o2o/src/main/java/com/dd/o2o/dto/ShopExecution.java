package com.dd.o2o.dto;

import java.util.List;

import com.dd.o2o.entity.Shop;
import com.dd.o2o.enums.ShopStateEnum;

public class ShopExecution {
	// 结果状态
	private int state;
	// 状态标识
	private String stateInfo;
	// 店铺数量
	private int count;
	// 操作的店铺
	private Shop shop;
	// 店铺列表（查询时用）
	private List<Shop> shopList;

	public ShopExecution() {

	}

	// 失败的构造器
	public ShopExecution(ShopStateEnum stateEnum) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
	}

	// 成功的构造器
	public ShopExecution(ShopStateEnum stateEnum, Shop shop) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shop = shop;
	}

	// 成功的构造器
	public ShopExecution(ShopStateEnum stateEnum, List<Shop> shopList) {
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getStateInfo();
		this.shopList = shopList;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public int getCount() {
		return count;
	}

	public Shop getShop() {
		return shop;
	}

	public List<Shop> getShopList() {
		return shopList;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public void setShopList(List<Shop> shopList) {
		this.shopList = shopList;
	}

}
