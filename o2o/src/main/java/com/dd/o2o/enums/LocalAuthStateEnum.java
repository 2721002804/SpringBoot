package com.dd.o2o.enums;

public enum LocalAuthStateEnum {
	CHECK(0, "审核中"), OFFLINE(-1, "非法店铺"), SUCCESS(1, "操作成功"), PASS(2, "通过认证"), INNER_ERROR(-1001,
			"内部系统错误"), ONLY_ONE_ACCOUNT(-1002, "该账号已存在"), NULL_AUTH_INFO(-1003, "localAuth信息为空");
	private int state;
	private String stateInfo;

	private LocalAuthStateEnum(int state, String stateInfo) {
		this.state = state;
		this.stateInfo = stateInfo;
	}

	// 根据传入的state返回相应的enum值
	public static LocalAuthStateEnum stateOf(int state) {
		for (LocalAuthStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

	public int getState() {
		return state;
	}

	public String getStateInfo() {
		return stateInfo;
	}

}
