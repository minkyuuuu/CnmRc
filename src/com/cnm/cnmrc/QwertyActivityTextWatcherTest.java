package com.cnm.cnmrc;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cnm.cnmrc.custom.ExtendedEditText;
import com.cnm.cnmrc.custom.ExtendedEditTextTextWatcherTest;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.TouchHandler;
import com.google.android.apps.tvremote.TouchHandler.Mode;
import com.google.android.apps.tvremote.util.Action;

public class QwertyActivityTextWatcherTest extends BaseActivity {

	ExtendedEditTextTextWatcherTest edit;
	Button mQwertyCancel, mQwertyTextClear;
	private TextInputHandler textInputHandler = null;

	/**
	 * The main view.
	 */
	private View view;

	public QwertyActivityTextWatcherTest() {
		//textInputHandler = new TextInputHandler(this, getCommands());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qwerty);

		// hwang 2013-11-28 add
		// MainActivity와 별개의 BaseActivity를 사용한다.
		CnmPreferences pref = CnmPreferences.getInstance();
		if (pref.loadFirstConnectGtv(getApplicationContext())) {
			connect();
		}

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		edit = (ExtendedEditTextTextWatcherTest) findViewById(R.id.qwerty_edit);
		edit.setPrivateImeOptions("defaultInputmode=english"); // not working
		//edit.setKeyListener(null);
		//edit.setEnabled(false);
		//edit.setFocusable(true);
		//edit.setFocusableInTouchMode(true);
		//edit.requestFocus();
		//setOnKeyListener() --> hardware keyboard에만 적용되는것 같다.
		
//		edit.setOnTouchListener(new View.OnTouchListener() {
//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			switch(event.getAction()){
//			case MotionEvent.ACTION_UP:
//				//Selection.extendSelection(edit.getText(), edit.getText().length());
//				//edit.setSelection(null, edit.getText().length());
//				//edit.setSelection(0);
//				return true;
//			}
//			
//			return false;
//		}
//	});

//		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					Action.ENTER.execute(getCommands());
//					finish();
//
//					/*
//					 * String str = v.getText().toString(); if (str.equals(""))
//					 * { Toast.makeText(QwertyActivity.this, "입력해 주세요",
//					 * Toast.LENGTH_SHORT).show(); } else { performDone(); }
//					 */
//					return true;
//				}
//				return false;
//			}
//
//		});

		((ExtendedEditTextTextWatcherTest) edit).setInterceptor(new ExtendedEditTextTextWatcherTest.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivityTextWatcherTest.this.onUserInteraction();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK: // 4
						finish();
						return true;

						// not working on this status
					case KeyEvent.KEYCODE_ENTER: // 66
						Action.ENTER.execute(getCommands());
						finish();
						return true;

						// not working on this status
					case KeyEvent.KEYCODE_DEL: // 67
						Action.DEL.execute(getCommands());
						return true;
					}
				}

				return false;
			}

			public boolean onSymbol(char c) {
				//QwertyActivity.this.onUserInteraction();
				//handleChar(c);
				return false;
			}
		});

		edit.addTextChangedListener(onEditTextChanged);

		// Attach touch handler to the touch pad.
		view = (View) findViewById(R.id.view);
		new TouchHandler(view, Mode.POINTER_MULTITOUCH, getCommands());

	}

	int beforeCount = 0;
	int afterCount = 0;
	String beforeText = "";
	String afterText = "";
	String strAmount = ""; // 임시 저장값 (콤마)
	
	// back key는 EditText에 데이타가 없으면 listener에 잡히지 않는다.
	// 한영모드가 변하면 start 값이 변한다. 숫자는 start 값이 변하지 않는다.
	// 자판이 변하면 숫자도 start값이 변한다.
	// 특수문자는 무조건 start값이 변한다.
	//@SuppressLint("NewApi")
	TextWatcher onEditTextChanged = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			Log.v("hwang2", "beforeTextChanged : start : " + start);	// cursor의 위치를 의미한다. 처음에는 0이다. 한영모드가 변하면 start 값이 변한다.
			Log.v("hwang2", "beforeTextChanged : count : " + count);	// cursor를 중심으로해서 앞의 글자수를 의미한다.
			Log.v("hwang2", "beforeTextChanged : after : " + after);
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Log.v("hwang2", "onTextChanged : start : " + start);		// 항상 0
			Log.v("hwang2", "onTextChanged : count : " + count);
			Log.v("hwang2", "onTextChanged : before : " + before);
			//if (count == 3) edit.setSelection(0);

			//			beforeCount = beforeText.length();
			//			afterCount = s.length();
			//
			//			if (afterCount < beforeCount) {
			//				Action.DEL.execute(getCommands());
			//			} else {
			//				CharSequence c = s.subSequence(start, start + 1);
			//				handleChar(c.toString());
			//			}

			//			if (!s.toString().equals(strAmount)) { // StackOverflow 방지
			//				strAmount = makeStringComma(s.toString().replace(",", ""));
			//				edit.setText(strAmount);
			//				Editable e = edit.getText();
			//				Selection.setSelection(e, strAmount.length());
			//			}

		}

		public void afterTextChanged(Editable s) {
			Log.v("hwang2", "afterTextChanged : " + s);
			Log.v("hwang2", "length() : " + s.length());
			Log.v("hwang2", "-------------------" + s);
			
		}

	};
	
    private void printIt(String string) {
    	Log.v("hwang2", "-------------------" + string);
        for (int i = 0; i < string.length(); i++) {
            Log.v("hwang2", "-------------------" + String.format("U+%04X ", string.codePointAt(i)));
        }
        Log.v("hwang2", "-------------------" + string);
    }

	protected String makeStringComma(String str) { // 천단위 콤마 처리
		if (str.length() == 0)
			return "";
		long value = Long.parseLong(str);
		DecimalFormat format = new DecimalFormat("###,###");
		return format.format(value);
	}

	public boolean handleChar(char c) {
		if (isValidCharacter(c)) {
			String str = String.valueOf(c);
			//appendDisplayedText(str);
			getCommands().string(str);
			return true;
		}
		return false;
	}

	public boolean handleChar(String str) {
		getCommands().string(str);
		return false;
	}

	private boolean isValidCharacter(int unicode) {
		//return unicode > 0 && unicode < 256;
		return (unicode > 0 && unicode < 256) || (unicode > 12591 && unicode < 12688);
	}

	private String appendDisplayedText(CharSequence seq) {
		//seq = new StringBuffer(edit.getText()).append(seq);
		//edit.setText(seq);
		return String.valueOf(seq);
	}

	private void performDone() {
		Toast.makeText(QwertyActivityTextWatcherTest.this, "완료", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onTrackballEvent(event);
	}

}
