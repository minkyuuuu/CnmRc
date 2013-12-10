package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;

public class PopupGtvConnection extends PopupBase2 {

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
		
		mTitle.setText(getString(R.string.popup_gtv_connection_title));
		
		mLine1.setText(getString(R.string.popup_gtv_connection_line_1));
		mLine2.setText(getString(R.string.popup_gtv_connection_line_2));
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		Log.d("hwang-tvremote", "PopupGtvConnection : onClick()");
		
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			Log.i("hwang", "(search)yes button pressed");
			//((MainActivity) getActivity()).openGtvConnection();
			((MainActivity) getActivity()).connect();
			
			CnmPreferences pref = CnmPreferences.getInstance();
			pref.saveFirstConnectGtv(getActivity().getApplicationContext(), true);
			
			// hwang 2013-11-20
			// pairing host address is reset.
			pref.savePairingHostAddress(getActivity().getApplicationContext(), "");
			
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			
			// hwang 2013-11-28 adding
			pref = CnmPreferences.getInstance();
			pref.saveFirstConnectGtv(getActivity().getApplicationContext(), false);
			
			break;
		}

	}

}
