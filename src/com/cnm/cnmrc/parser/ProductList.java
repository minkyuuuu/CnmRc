package com.cnm.cnmrc.parser;

import java.util.ArrayList;

public class ProductList {

	private String resultCode;
	private ArrayList<Product> list;
	
	// construct
	public ProductList() {
		list = new ArrayList<Product>();
	}
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	
	public ArrayList<Product> getList() {
		return list;
	}
	public void setList(ArrayList<Product> list) {
		this.list = list;
	}


}
