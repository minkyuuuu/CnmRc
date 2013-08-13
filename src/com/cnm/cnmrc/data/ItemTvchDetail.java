package com.cnm.cnmrc.data;


public class ItemTvchDetail extends ItemRoot{
	
	int		channel_no;
	String 	channel_name;
	
	String  date;
	
	String 	current_time;
	String 	current_title;
	
	int		hdIconResId;
	
	
	public int getChannel_no() {
		return channel_no;
	}
	public void setChannel_no(int channel_no) {
		this.channel_no = channel_no;
	}
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCurrent_time() {
		return current_time;
	}
	public void setCurrent_time(String current_time) {
		this.current_time = current_time;
	}
	public String getCurrent_title() {
		return current_title;
	}
	public void setCurrent_title(String current_title) {
		this.current_title = current_title;
	}
	public int getHdIconResId() {
		return hdIconResId;
	}
	public void setHdIconResId(int hdIconResId) {
		this.hdIconResId = hdIconResId;
	}
	
	
	
}


