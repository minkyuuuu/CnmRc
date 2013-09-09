package com.cnm.cnmrc.item;

import java.util.ArrayList;


public class ItemChannelAreaList {

	private String resultCode;
	
	private ArrayList<ItemChannelArea> list;
	
	
	
	// construct
	public ItemChannelAreaList() {
		list = new ArrayList<ItemChannelArea>();
	}
	
	
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<ItemChannelArea> getList() {
		return list;
	}

	public void setList(ArrayList<ItemChannelArea> areaList) {
		this.list = areaList;
	}


}
