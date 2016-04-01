package com.gaokan.essay.bean;

import java.util.List;

public class EssayData {
	private int essayId;
	private List<String> essayKeys;
	private List<String> essayProduct;
	private String essayTitle;
	private String essayData;
	public int getEssayId() {
		return essayId;
	}
	public void setEssayId(int essayId) {
		this.essayId = essayId;
	}
	public List<String> getEssayKeys() {
		return essayKeys;
	}
	public void setEssayKeys(List<String> essayKeys) {
		this.essayKeys = essayKeys;
	}
	public List<String> getEssayProduct() {
		return essayProduct;
	}
	public void setEssayProduct(List<String> essayProduct) {
		this.essayProduct = essayProduct;
	}
	public String getEssayTitle() {
		return essayTitle;
	}
	public void setEssayTitle(String essayTitle) {
		this.essayTitle = essayTitle;
	}
	public String getEssayData() {
		return essayData;
	}
	public void setEssayData(String essayData) {
		this.essayData = essayData;
	}
}
