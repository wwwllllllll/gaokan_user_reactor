package com.gaokan.user.bean;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Vendor {
	private static final AtomicLong COUNTER = new AtomicLong();
	private long id;
	private String vendorName;
	private String logoLink;
	private List<Coupon> coupons;
	public void generateId() {
		id = COUNTER.getAndIncrement();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public String getLogoLink() {
		return logoLink;
	}
	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
	}
	public List<Coupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
}
