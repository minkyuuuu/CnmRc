package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemChannelProductList {

	private String resultCode;
	
	private ArrayList<ItemChannelProduct> list;
	
	
	
	// construct
	public ItemChannelProductList() {
		list = new ArrayList<ItemChannelProduct>();
	}
	
	
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<ItemChannelProduct> getList() {
		return list;
	}
	public void setList(ArrayList<ItemChannelProduct> list) {
		this.list = list;
	}


}
