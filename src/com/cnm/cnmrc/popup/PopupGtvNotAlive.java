package com.cnm.cnmrc.popup;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.util.Util;

public class PopupGtvNotAlive extends PopupBase3 {
	
	public static PopupGtvNotAlive newInstance(String assetId) {
		PopupGtvNotAlive f = new PopupGtvNotAlive();
		Bundle args = new Bundle();
		args.putString("assetId", assetId);
		f.setArguments(args);
		return f;
	}
	
	String assetId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

		assetId = getArguments().getString("assetId");
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		mTitle.setText(getString(R.string.popup_gtv_not_alive_title));
		
		mLine1.setText(getString(R.string.popup_gtv_not_alive_input_line_1));
		mLine2.setText(getString(R.string.popup_gtv_not_alive_input_line_2));
		mLine3.setText(getString(R.string.popup_gtv_not_alive_input_line_3));
		
		mNo.setVisibility(View.GONE);
		
		super.onActivityCreated(arg0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			Log.i("hwang", "폰에 저장후 일괄 처리됩니다.");
			Util.insertDBVodJjim(getActivity(), assetId);		// db insert
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}

}
