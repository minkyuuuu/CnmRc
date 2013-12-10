package com.cnm.cnmrc.popup;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.PairingActivity;

public class PopupGtvWaitingPairingCode extends PopupBase2 {

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
		mTitle.setText(getString(R.string.popup_gtv_waiting_pairing_code_title));

		mLine1.setText(getString(R.string.popup_gtv_waiting_pairing_code_line_1));
		mLine2.setText(getString(R.string.popup_gtv_waiting_pairing_code_line_2));

		mProgressBar.setVisibility(View.VISIBLE);
		mProgressBar.startAnimation(animation);
		mYes.setVisibility(View.GONE);

		super.onActivityCreated(arg0);
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

	@Override
	public void onDestroyView() {
		((PairingActivity) getActivity()).hideKeyboard();
		super.onDestroyView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		getDialog().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// disable search button action
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					closeDialog();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			break;
		case R.id.popup_no:
			closeDialog();
			break;
		}

	}

	private void closeDialog() {
		((PairingActivity) getActivity()).cancelPairing();
		((PairingActivity) getActivity()).hideKeyboard();
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
	}


}
