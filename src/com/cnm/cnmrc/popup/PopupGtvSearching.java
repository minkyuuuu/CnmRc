package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.DeviceFinder;

public class PopupGtvSearching extends PopupBase {
	
	
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
		mTitle.setText(getString(R.string.popup_gtv_searching_title));
		
		mLine1.setText(getString(R.string.popup_gtv_searching_line_1));
		mLine2.setText(getString(R.string.popup_gtv_searching_line_2));
		
		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.startAnimation(animation);
		mYes.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			break;
		case R.id.popup_no:
			((DeviceFinder)getActivity()).removeDelayedMessage(); // when cancel button is clicked!!!
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}

}
