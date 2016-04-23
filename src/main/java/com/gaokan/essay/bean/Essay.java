package com.gaokan.essay.bean;

import java.util.concurrent.atomic.AtomicLong;

public class Essay {
	private static final AtomicLong COUNTER = new AtomicLong();
	private long id;
	private String userId;
	private int essayType;
	private int essayId;
	private String essayTitle;
	private String digest;
	private String date;
	public void generateId() {
		id = COUNTER.getAndIncrement();
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getEssayType() {
		return essayType;
	}
	public void setEssayType(int essayType) {
		this.essayType = essayType;
	}
	public int getEssayId() {
		return essayId;
	}
	public void setEssayId(int essayId) {
		this.essayId = essayId;
	}
	public String getEssayTitle() {
		return essayTitle;
	}
	public void setEssayTitle(String essayTitle) {
		this.essayTitle = essayTitle;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
