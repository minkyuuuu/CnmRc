package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.config.ConfigMain;

public class PopupConfigAdultName extends PopupBase2 {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		mTitle.setText(getString(R.string.popup_gtv_not_alive_title));
		
		mLine1.setText(getString(R.string.popup_config_adult_name_line_1));
		mLine2.setText(getString(R.string.popup_config_adult_name_line_2));
		
		mNo.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}

}
