package com.cnm.cnmrc.http;

import java.util.ArrayList;

public class SearchVodList {

	private String resultCode = "";
	private String errorString = "";
	private String totalCount = "";
	private String totalPage = "";
	
	private ArrayList<SearchVod> list;

	
	// construct
	public SearchVodList() {
		list = new ArrayList<SearchVod>();
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

	public ArrayList<SearchVod> getList() {
		return list;
	}

	public void setList(ArrayList<SearchVod> list) {
		this.list = list;
	}
	
	
	

}
