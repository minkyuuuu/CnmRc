package com.cnm.cnmrc.parser;

import java.util.ArrayList;


public class ChannelAreaList {

	private String resultCode;
	private ArrayList<ChannelArea> list;
	
	// construct
	public ChannelAreaList() {
		list = new ArrayList<ChannelArea>();
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<ChannelArea> getList() {
		return list;
	}

	public void setList(ArrayList<ChannelArea> areaList) {
		this.list = areaList;
	}


}
