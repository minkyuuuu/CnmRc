package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.config.ConfigMain;
import com.cnm.cnmrc.popup.PopupConfigAdultCertSucceed.Interceptor;

public class PopupConfigAdultCertFail extends PopupBase1 {
	
	public interface Interceptor {
		public void onGoToConfigMain();
	}
	
	private Interceptor interceptor;
	
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
		
		mLine1.setText(getString(R.string.popup_config_adult_result_fail_line_1));
		
		mNo.setVisibility(View.GONE);
		
		setCancelable(false);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			interceptor.onGoToConfigMain();
			break;
		}
	}
	
	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

}
