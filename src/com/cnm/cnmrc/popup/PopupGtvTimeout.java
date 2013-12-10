package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.DeviceFinder;

public class PopupGtvTimeout extends PopupBase2 {
	
	
//	public static PopupGtvSearching newInstance(String type) {
//		PopupGtvSearching f = new PopupGtvSearching();
//		Bundle args = new Bundle();
//		args.putString("type", type);
//		f.setArguments(args);
//		return f;
//	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		mTitle.setText(getString(R.string.popup_gtv_timeout_title));
		
		mLine1.setText(getString(R.string.popup_gtv_timeout_line_1));
		mLine2.setText(getString(R.string.popup_gtv_timeout_line_2));
		
		mNo.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			// hwang 2013-11-28 adding
			// 계속해서 찾기 팝업창이 보인다.
			CnmPreferences pref = CnmPreferences.getInstance();
			pref.saveFirstConnectGtv(getActivity().getApplicationContext(), false);
			
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			getActivity().setResult(getActivity().RESULT_CANCELED, null);
			getActivity().finish();
			break;
		case R.id.popup_no:
			break;
		}

	}

}
