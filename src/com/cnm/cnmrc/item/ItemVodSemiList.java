package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemVodSemiList {

	private String resultCode = "";
	private String errorString = "";
	private String totalCount = "";
	private String totalPage = "";
	
	private ArrayList<ItemVodSemi> list;

	
	// construct
	public ItemVodSemiList() {
		list = new ArrayList<ItemVodSemi>();
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

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public ArrayList<ItemVodSemi> getList() {
		return list;
	}

	public void setList(ArrayList<ItemVodSemi> list) {
		this.list = list;
	}
	
	
	

}
