package com.cnm.cnmrc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class QwertyFragment extends DialogFragment {

	public interface CallbackQwerty {
		@SuppressLint("NewApi")
		public void onEventQwertySelected();
	}

	private CallbackQwerty mCallbackQwerty = sDummyCallback;

	@Override
	public void onAttach(Activity activity) {
		mCallbackQwerty = (CallbackQwerty) activity;
		
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
		View layout = (View) inflater.inflate(R.layout.activity_qwerty, container, false);

		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		EditText editText = (EditText) layout.findViewById(R.id.qwerty);
		//editText.requestFocus();
		//InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
		//imm.showSoftInputFromInputMethod (editText .getApplicationWindowToken(),InputMethodManager.SHOW_FORCED);
		
		InputMethodManager mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		//mInputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		
		return layout;
	}
	
	@Override
	public void onActivityCreated(Bundle arg0) {
		Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
            	 mCallbackQwerty.onEventQwertySelected();
            }
        };
         
        Handler mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 200);
        
		super.onActivityCreated(arg0);
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}





	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		
		mCallbackQwerty = null;
	}

	private static CallbackQwerty sDummyCallback = new CallbackQwerty() {

		@Override
		public void onEventQwertySelected() {
			// TODO Auto-generated method stub

		}

	};
}
