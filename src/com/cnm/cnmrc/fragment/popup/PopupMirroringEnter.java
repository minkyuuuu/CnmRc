package com.cnm.cnmrc.fragment.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;

public class PopupMirroringEnter extends PopupBase {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		mTitle.setText(getString(R.string.popup_mirroring_title));
		
		mLine1.setText(getString(R.string.popup_mirroring_enter_line_1));
		mLine1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getFraction(R.dimen.sp30sp, 1, 1));
		
		mLine2.setText(getString(R.string.popup_mirroring_enter_line_2));
		mLine2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getFraction(R.dimen.sp30sp, 1, 1));
		
		super.onActivityCreated(arg0);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			if(getActivity() instanceof MainActivity) {
				getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			    ((MainActivity) getActivity()).openMirroring();
			}
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			Log.i("hwang", "(mirroring enter)no button pressed");
			break;
		}

	}

}
