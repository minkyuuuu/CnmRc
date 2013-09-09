package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemSearchProgramList {
	
	private String resultCode = "";
	private String errorString = "";
	private String totalCount = "";
	private String totalPage = "";
	private String areaCode = "";
	private String productCode = "";
	
	private ArrayList<ItemSearchProgram> list;
	
	
	
	// construct
	public ItemSearchProgramList() {
		list = new ArrayList<ItemSearchProgram>();
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

	public ArrayList<ItemSearchProgram> getList() {
		return list;
	}

	public void setList(ArrayList<ItemSearchProgram> list) {
		this.list = list;
	}


}
