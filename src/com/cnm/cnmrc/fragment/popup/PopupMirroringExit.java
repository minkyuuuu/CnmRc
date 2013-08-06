package com.cnm.cnmrc.fragment.popup;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cnm.cnmrc.MirroringActivity;
import com.cnm.cnmrc.R;

public class PopupMirroringExit extends DialogFragment implements View.OnClickListener {
	TextView mTitle, mLine1, mLine2;
	Button mYes, mNo;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.popup, container, false);
		
		mTitle = (TextView) layout.findViewById(R.id.popup_title);
		mTitle.setText(getString(R.string.mirroring_enter_title));
		mLine1 = (TextView) layout.findViewById(R.id.popup_line_1);
		mLine1.setText(getString(R.string.mirroring_exit));
		mLine2 = (TextView) layout.findViewById(R.id.popup_line_2);
		mLine2.setText("");

		mYes = (Button) layout.findViewById(R.id.popup_yes);
		mYes.setText(getString(R.string.popup_yes));
		mYes.setOnClickListener(this);
		
		mNo = (Button) layout.findViewById(R.id.popup_no);
		mNo.setText(getString(R.string.popup_no));
		mNo.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onActivityCreated(arg0);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);

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
			Log.i("hwang", "no button pressed");
			break;
		}

	}

}
