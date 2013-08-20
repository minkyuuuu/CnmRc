package com.cnm.cnmrc.fragment.popup;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.MirroringActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.config.ConfigFragment;

public class PopupMirroringExit extends PopBase {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar_Fullscreen;
		setStyle(style, theme);

	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		mTitle.setText(getString(R.string.popup_mirroring_title));
		
		mLine1.setText(getString(R.string.popup_mirroring_exit_line_1));
		mLine1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getFraction(R.dimen.sp30sp, 1, 1));
		
		mLine2.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			if(getActivity() instanceof MirroringActivity) {
				getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			    ((MirroringActivity) getActivity()).finish();
			}
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}
	}

}
