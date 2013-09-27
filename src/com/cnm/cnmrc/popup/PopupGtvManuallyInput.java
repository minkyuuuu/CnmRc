package com.cnm.cnmrc.popup;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.DeviceFinder;
import com.google.android.apps.tvremote.RemoteDevice;

public class PopupGtvManuallyInput extends DialogFragment implements View.OnClickListener {

	TextView mTitle, mLine1, mLine2;
	EditText mEdit;
	Button mYes, mNo;

	Context context;

	@Override
	public void onAttach(Activity activity) {
		context = activity;

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
		View layout = (View) inflater.inflate(R.layout.popup_edittext, container, false);

		mTitle = (TextView) layout.findViewById(R.id.popup_title);
		mTitle.setText(getString(R.string.popup_gtv_manually_input_title));

		mEdit = (EditText) layout.findViewById(R.id.popup_edit);
		mEdit.setFilters(new InputFilter[] { new NumberKeyListener() {
			@Override
			protected char[] getAcceptedChars() {
				return "0123456789.:".toCharArray();
			}

			public int getInputType() {
				return InputType.TYPE_CLASS_NUMBER;
			}
		} });
		mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					String str = v.getText().toString();
					if (str.equals("")) {
						Toast.makeText(getActivity(), getString(R.string.manual_ip_no_address), Toast.LENGTH_SHORT).show();
					} else {
						performAction();
					}

					return true;
				}
				return false;
			}

		});

		mLine1 = (TextView) layout.findViewById(R.id.popup_line_1);
		mLine1.setText(getString(R.string.popup_gtv_manually_input_line_1));
		mLine2 = (TextView) layout.findViewById(R.id.popup_line_2);
		mLine2.setText(getString(R.string.popup_gtv_manually_input_line_2));

		mYes = (Button) layout.findViewById(R.id.popup_yes);
		mYes.setText(getString(R.string.popup_yes));
		mYes.setOnClickListener(this);

		mNo = (Button) layout.findViewById(R.id.popup_no);
		mYes.setText(getString(R.string.popup_no));
		mNo.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.popup_yes:
			if (mEdit.getText().length() < 1) {
				Toast.makeText(getActivity(), getString(R.string.manual_ip_no_address), Toast.LENGTH_SHORT).show();
				//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
				break;
			}
			
			performAction();
			break;
		case R.id.popup_no:
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			break;
		}

	}
	
	private void performAction() {
		RemoteDevice remoteDevice = ((DeviceFinder)getActivity()).remoteDeviceFromString(mEdit.getText().toString());
		if (remoteDevice != null) {
			((DeviceFinder)getActivity()).connectToEntry(remoteDevice);
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			getActivity().finish();
		} else {
			Toast.makeText(getActivity(), getString(R.string.manual_ip_error_address), Toast.LENGTH_SHORT).show();
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
			//getActivity().finish();
		}
	}

}
