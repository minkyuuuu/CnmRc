package com.cnm.cnmrc.item;

import java.util.ArrayList;

public class ItemSearchNaverList {

	private String total = "";
	
	private ArrayList<ItemSearchNaver> list;

	
	
	// construct
	public ItemSearchNaverList() {
		list = new ArrayList<ItemSearchNaver>();
	}


	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}

	

	public ArrayList<ItemSearchNaver> getList() {
		return list;
	}
	public void setList(ArrayList<ItemSearchNaver> list) {
		this.list = list;
	}

}
