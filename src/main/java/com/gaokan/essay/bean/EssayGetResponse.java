package com.gaokan.essay.bean;

public class EssayGetResponse {
	private int state;
	private EssayData data;
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public EssayData getData() {
		return data;
	}
	public void setData(EssayData data) {
		this.data = data;
	}
}