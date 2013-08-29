package com.cnm.cnmrc.parser;

import java.util.ArrayList;


public class AreaList {

	private String resultCode;
	private ArrayList<Area> list;
	
	// construct
	public AreaList() {
		list = new ArrayList<Area>();
	}
	
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<Area> getList() {
		return list;
	}

	public void setList(ArrayList<Area> areaList) {
		this.list = areaList;
	}


}
