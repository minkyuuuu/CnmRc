package com.cnm.cnmrc.parser;

import java.util.ArrayList;

public class SearchProgramList {
	
	private String resultCode = "";
	private String errorString = "";
	private String totalCount = "";
	private String totalPage = "";
	private String areaCode = "";
	private String productCode = "";
	
	private ArrayList<SearchProgram> list;
	
	
	
	// construct
	public SearchProgramList() {
		list = new ArrayList<SearchProgram>();
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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public ArrayList<SearchProgram> getList() {
		return list;
	}

	public void setList(ArrayList<SearchProgram> list) {
		this.list = list;
	}


}
