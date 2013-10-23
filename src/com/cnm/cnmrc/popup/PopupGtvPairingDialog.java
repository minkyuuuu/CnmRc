package com.cnm.cnmrc.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
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
import com.cnm.cnmrc.util.Util;
import com.google.android.apps.tvremote.DeviceFinder;
import com.google.android.apps.tvremote.PairingActivity;
import com.google.android.apps.tvremote.PairingActivity.PairingClientThread;
import com.google.android.apps.tvremote.RemoteDevice;

@SuppressLint("ValidFragment")
public class PopupGtvPairingDialog extends DialogFragment implements View.OnClickListener {
	
//	public static PopupGtvPairingDialog newInstance(PairingClientThread c) {
//		PopupGtvPairingDialog f = new PopupGtvPairingDialog();
//		client = c;
//		return f;
//	}

	TextView mTitle, mLine1, mLine2;
	EditText mEdit;
	Button mYes, mNo;
	
	static PairingClientThread client;

	Context context;
	
	public PopupGtvPairingDialog(PairingClientThread c) {
		client = c;
	}

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
		View layout = (View) inflater.inflate(R.layout.popup_ip_input, container, false);

		mTitle = (TextView) layout.findViewById(R.id.popup_title);
		mTitle.setText(getString(R.string.popup_gtv_pairing_dialog_title));

		mEdit = (EditText) layout.findViewById(R.id.popup_edit);
		mEdit.setHint(getString(R.string.pairing_code_hint));
//		mEdit.setFilters(new InputFilter[] { new NumberKeyListener() {
//			@Override
//			protected char[] getAcceptedChars() {
//				return "0123456789abcdefABCDEF:".toCharArray();
//			}
//
//			public int getInputType() {
//				//return InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_TEXT;
//				return InputType.TYPE_CLASS_TEXT;
//			}
//		} });
		mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {

					String str = v.getText().toString();
					if (str.equals("")) {
						Toast.makeText(getActivity(), getString(R.string.no_pairing_code), Toast.LENGTH_SHORT).show();
					} else {
						performAction();
					}

					return true;
				}
				return false;
			}

		});

		mLine1 = (TextView) layout.findViewById(R.id.popup_line_1);
		mLine1.setText(((PairingActivity)getActivity()).getRemoteDeviceName());
		
		mLine2 = (TextView) layout.findViewById(R.id.popup_line_2);
		mLine2.setVisibility(View.GONE);

		mYes = (Button) layout.findViewById(R.id.popup_yes);
		mYes.setText(getString(R.string.popup_yes));
		mYes.setOnClickListener(this);

		mNo = (Button) layout.findViewById(R.id.popup_no);
		mNo.setText(getString(R.string.popup_no));
		mNo.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
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
	public void onDestroyView() {
//		((PairingActivity)getActivity()).hideKeyboard();
		Util.hideSoftKeyboard((PairingActivity)getActivity());
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		((PairingActivity)getActivity()).makeAlertDialogNull();
		
		switch (v.getId()) {
		case R.id.popup_yes:
			if (mEdit.getText().length() < 1) {
				Toast.makeText(getActivity(), getString(R.string.no_pairing_code), Toast.LENGTH_SHORT).show();
				//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
				break;
			}
			
			performAction();
			break;
		case R.id.popup_no:
			closeDialog();
			break;
		}

	}
	
	private void performAction() {
		((PairingActivity)getActivity()).hideKeyboard();
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		client.setSecret(mEdit.getText().toString());
	}
	
	private void closeDialog() {
		((PairingActivity) getActivity()).hideKeyboard();
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		client.cancel();
	}

}
