package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.config.ConfigAdultCert;
import com.cnm.cnmrc.fragment.config.ConfigMain;
import com.cnm.cnmrc.popup.PopupConfigAdultCertFail.Interceptor;

public class PopupConfigAdultCertNeed extends PopupBase22 {
	
	public interface Interceptor {
		public void onReturnVodSemi(boolean isAdultCert);
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
		
		mLine1.setText(getString(R.string.popup_config_adult_cert_need_line_1));
		mLine2.setText(getString(R.string.popup_config_adult_cert_need_line_2));
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			interceptor.onReturnVodSemi(true);
			
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
