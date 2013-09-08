package com.cnm.cnmrc.parser;

import java.util.ArrayList;

public class SearchNaverList {

	private String total = "";
	
	private ArrayList<SearchNaver> list;

	
	
	// construct
	public SearchNaverList() {
		list = new ArrayList<SearchNaver>();
	}


	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

	

	public ArrayList<SearchNaver> getList() {
		return list;
	}
	public void setList(ArrayList<SearchNaver> list) {
		this.list = list;
	}

}
