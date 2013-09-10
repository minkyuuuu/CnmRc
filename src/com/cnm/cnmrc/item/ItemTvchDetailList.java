package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemTvchDetailList {
	
	private String resultCode = "";
	private String errorString = "";
	private String dateCount = "";
	
	private ArrayList<ItemTvchDetail> list;
	
	
	
	// construct
	public ItemTvchDetailList() {
		list = new ArrayList<ItemTvchDetail>();
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

	public String getDateCount() {
		return dateCount;
	}

	public void setDateCount(String dateCount) {
		this.dateCount = dateCount;
	}



	public ArrayList<ItemTvchDetail> getList() {
		return list;
	}

	public void setList(ArrayList<ItemTvchDetail> list) {
		this.list = list;
	}

	
	


}
