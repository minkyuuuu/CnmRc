package com.cnm.cnmrc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cnm.cnmrc.popup.PopupTvReservingConfirm;

public class TvReservingConfirm extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tvreserving_confirm);
		
		// ok
		Bundle extras = getIntent().getExtras();
		String title = extras.getString("title");
		String time = extras.getString("time");
		
		// ok
//		Intent intent = getIntent();
//		String title = getIntent().getStringExtra("title");
//		String time = getIntent().getStringExtra("time");
		
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		PopupTvReservingConfirm popup = new PopupTvReservingConfirm(title, time);
		popup.show(ft, PopupTvReservingConfirm.class.getSimpleName());

	}


}
