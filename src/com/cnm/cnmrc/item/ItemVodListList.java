package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemVodListList {
	
	private String resultCode = "";
	private String errorString = "";

	private ArrayList<ItemVodList> list;

	
	
	// construct
	public ItemVodListList() {
		list = new ArrayList<ItemVodList>();
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

	public ArrayList<ItemVodList> getList() {
		return list;
	}
	public void setList(ArrayList<ItemVodList> list) {
		this.list = list;
	}

}
