package com.gaokan.essay.bean;

public class EssayGetRequest {
	private String userId;
	private int essayType;
	private int essayId;
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
}
