package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.ExtendedEditText.Interceptor;
import com.cnm.cnmrc.fragment.vodtvch.TvchDetail;
import com.cnm.cnmrc.util.CnmPreferences;

public class PopupTvReserving extends PopupBase {
	
//	public static PopupTvReserving newInstance(String title, String time, TvchDetail tvchDetail) {
//		PopupTvReserving f = new PopupTvReserving();
//		Bundle args = new Bundle();
//		args.putString("title", title);
//		args.putString("time", time);
//		f.setArguments(args);
//		return f;
//	}
	
	public interface Interceptor {
		public void onSetAlarm();
	}
	
	String title ="";
	String time ="";
	TvchDetail tvchDetail;
	private Interceptor interceptor;
	
	public PopupTvReserving(String title, String time) {
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
		
//		title = getArguments().getString("title");
//		time = getArguments().getString("time");

	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		Log.d("hwang-tvremote", "PopupGtvConnection : onActivityCreated()");
		
		mTitle.setText(getString(R.string.popup_tvreserving_title));
		
		mLine1.setText(title);
		mLine2.setText(time);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		Log.d("hwang-tvremote", "PopupGtvConnection : onClick()");
		
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			interceptor.onSetAlarm();
			break;
			
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}
	
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

}
