package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.ExtendedEditText.Interceptor;

public class PopupTvReservingConfirm extends PopupBase2 {
	
	String title ="";
	String time ="";
	
	public PopupTvReservingConfirm(String title, String time) {
		this.title = title;
		this.time = time;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("hwang-tvremote", "PopupGtvConnection : onCreate()");
		
		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);
		
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		Log.d("hwang-tvremote", "PopupGtvConnection : onActivityCreated()");
		
		mTitle.setText(getString(R.string.popup_tvreserving_confirm_title));
		
		mLine1.setText(title);
		mLine2.setText(time);
		
		mNo.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		Log.d("hwang-tvremote", "PopupGtvConnection : onClick()");
		
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			getActivity().finish();
			break;
			
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}

}
