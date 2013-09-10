package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemTvchListList {
	
	private String resultCode = "";
	private String errorString = "";

	private ArrayList<ItemTvchList> list;

	
	
	// construct
	public ItemTvchListList() {
		list = new ArrayList<ItemTvchList>();
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

	public ArrayList<ItemTvchList> getList() {
		return list;
	}
	public void setList(ArrayList<ItemTvchList> list) {
		this.list = list;
	}

}
