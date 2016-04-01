package com.gaokan.user.bean;

import java.util.concurrent.atomic.AtomicLong;

public class Coupon {
	private static final AtomicLong COUNTER = new AtomicLong();
	private long id;
	private String name;
	private String description;
	private String picLink;
	public void generateId() {
		id = COUNTER.getAndIncrement();
	}
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return id == ((Coupon)obj).getId();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicLink() {
		return picLink;
	}
	public void setPicLink(String picLink) {
		this.picLink = picLink;
	}
}
