package com.gaokan.user.bean;

public class UserAddCouponRequest {
	private String userCellNum;
	private long couponId;
	public String getUserCellNum() {
		return userCellNum;
	}
	public void setUserCellNum(String userCellNum) {
		this.userCellNum = userCellNum;
	}
	public long getCouponId() {
		return couponId;
	}
	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}
}
