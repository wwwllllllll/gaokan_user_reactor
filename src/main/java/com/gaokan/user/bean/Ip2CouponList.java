package com.gaokan.user.bean;

import java.util.List;

public class Ip2CouponList {
	String ip;
	List<Coupon> coupons;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
}
