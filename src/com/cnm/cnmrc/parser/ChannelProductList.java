package com.cnm.cnmrc.parser;

import java.util.ArrayList;

public class ChannelProductList {

	private String resultCode;
	private ArrayList<ChannelProduct> list;
	
	// construct
	public ChannelProductList() {
		list = new ArrayList<ChannelProduct>();
	}
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<ChannelProduct> getList() {
		return list;
	}
	public void setList(ArrayList<ChannelProduct> list) {
		this.list = list;
	}


}
