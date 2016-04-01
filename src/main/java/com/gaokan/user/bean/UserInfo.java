package com.gaokan.user.bean;

import java.util.List;

public class UserInfo {
	private String cellNum;
	private String password;
	private String nickName;
	private List<String> ipAddr;
	private List<Long> followVendors;
	private List<Coupon> coupons;
	public String getCellNum() {
		return cellNum;
	}
	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public List<String> getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(List<String> ipAddr) {
		this.ipAddr = ipAddr;
	}
	public List<Long> getFollowVendors() {
		return followVendors;
	}
	public void setFollowVendors(List<Long> followVendors) {
		this.followVendors = followVendors;
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
}
