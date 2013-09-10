package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemTvchSemiList {
	
	private String resultCode = "";
	private String errorString = "";
	private String areaCode = "";
	private String productCode = "";
	private String genreCode = "";
	
	private ArrayList<ItemTvchSemi> list;
	
	
	
	// construct
	public ItemTvchSemiList() {
		list = new ArrayList<ItemTvchSemi>();
	}
	
	
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public ArrayList<ItemTvchSemi> getList() {
		return list;
	}

	public void setList(ArrayList<ItemTvchSemi> list) {
		this.list = list;
	}

	public String getGenreCode() {
		return genreCode;
	}

	public void setGenreCode(String genreCode) {
		this.genreCode = genreCode;
	}
	
	


}
