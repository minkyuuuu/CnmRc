package com.cnm.cnmrc.data;


public class ItemVodDetail extends ItemRoot{

	String 	title;
	int		hdIconResId;
	int		ageResId;
	
	int		titleResId;
	
	String 	director;
	String 	director_name;
	String 	cast;
	String 	cast_name;
	String 	watching;
	String 	watching_rating;
	String 	price;
	String 	price_amount;
	
	int 	vod_selected;
	boolean	watching_at_tv;
	
	String	synopsis;
	
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHdIconResId() {
		return hdIconResId;
	}

	public void setHdIconResId(int hdIconResId) {
		this.hdIconResId = hdIconResId;
	}

	public int getAgeResId() {
		return ageResId;
	}

	public void setAgeResId(int ageResId) {
		this.ageResId = ageResId;
	}

	public int getTitleResId() {
		return titleResId;
	}

	public void setTitleResId(int titleResId) {
		this.titleResId = titleResId;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getDirector_name() {
		return director_name;
	}

	public void setDirector_name(String director_name) {
		this.director_name = director_name;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getCast_name() {
		return cast_name;
	}

	public void setCast_name(String cast_name) {
		this.cast_name = cast_name;
	}

	public String getWatching() {
		return watching;
	}

	public void setWatching(String watching) {
		this.watching = watching;
	}

	public String getWatching_rating() {
		return watching_rating;
	}

	public void setWatching_rating(String watching_rating) {
		this.watching_rating = watching_rating;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice_amount() {
		return price_amount;
	}

	public void setPrice_amount(String price_amount) {
		this.price_amount = price_amount;
	}

	public int getVod_selected() {
		return vod_selected;
	}

	public void setVod_selected(int vod_selected) {
		this.vod_selected = vod_selected;
	}

	public boolean isWatching_at_tv() {
		return watching_at_tv;
	}

	public void setWatching_at_tv(boolean watching_at_tv) {
		this.watching_at_tv = watching_at_tv;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	
	

	
}


