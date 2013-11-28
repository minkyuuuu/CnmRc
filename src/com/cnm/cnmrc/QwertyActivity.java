package com.cnm.cnmrc;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cnm.cnmrc.custom.ExtendedEditText;
import com.cnm.cnmrc.util.CnmPreferences;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.TouchHandlerA;
import com.google.android.apps.tvremote.TouchHandlerA.Mode;
import com.google.android.apps.tvremote.util.Action;

public class QwertyActivity extends BaseActivity {

	ExtendedEditText edit;
	Button mQwertyCancel, mQwertyTextClear;
	private TextInputHandler textInputHandler = null;

	/**
	 * The main view.
	 */
	private View view;

	public QwertyActivity() {
		//textInputHandler = new TextInputHandler(this, getCommands());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qwerty);
		
		// hwang 2013-11-28 add
		// MainActivity와 별개의 BaseActivity를 사용한다.
		CnmPreferences pref = CnmPreferences.getInstance();
		if(pref.loadFirstConnectGtv(getApplicationContext())) {
			connect();
		}
		
		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		edit = (ExtendedEditText) findViewById(R.id.qwerty_edit);
		edit.setFocusable(true);
		edit.setFocusableInTouchMode(true);
		edit.requestFocus();
		//edit.setPrivateImeOptions("defaultInputmode=english;symbol=false");

//		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//			@Override
//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					Action.ENTER.execute(getCommands());
//					//finish();
//					
//					String str = v.getText().toString();
//					if (str.equals("")) {
//						Toast.makeText(QwertyActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
//					} else {
//						performDone();
//					}
//					return true;
//				}
//				return false;
//			}
//
//		});
//
//		edit.setOnKeyListener(new OnKeyListener() {
//			@Override
//			public boolean onKey(View v, int keyCode, KeyEvent event) {
//				if (event.getAction() == KeyEvent.ACTION_DOWN) {
//					switch (event.getKeyCode()) {
//					case KeyEvent.KEYCODE_BACK:		// 4
//						finish();
//						return true;
//
//					case KeyEvent.KEYCODE_ENTER:	// 66
//						Action.ENTER.execute(getCommands());
//						finish();
//						return true;
//						
//					case KeyEvent.KEYCODE_DEL:		// 67
//						Action.BACKSPACE.execute(getCommands());
//						return true;
//					}
//				}
//				return false;
//			}
//		});

		edit.addTextChangedListener(onEditTextChanged);
		
		((ExtendedEditText) edit).setInterceptor(new ExtendedEditText.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivity.this.onUserInteraction();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK:		// 4
						finish();
						return true;

					case KeyEvent.KEYCODE_ENTER:	// 66
						Action.ENTER.execute(getCommands());
						finish();
						return true;
						
						// not working on this status
//					case KeyEvent.KEYCODE_DEL:		// 67
//						Action.BACKSPACE.execute(getCommands());
//						return true;
					}
				}

				return false;
				//return textInputHandler.handleKey(event);
			}

			public boolean onSymbol(char c) {
				//QwertyActivity.this.onUserInteraction();
				//textInputHandler.handleChar(c);
				handleChar(c);
				return false;
			}
		});
		
		//textInputHandler.setDisplay((TextView) findViewById(R.id.text_feedback_chars));

		// Attach touch handler to the touch pad.
		view = (View) findViewById(R.id.view);
		new TouchHandlerA(view, Mode.POINTER_MULTITOUCH, getCommands());

	}
	
	int beforeCount = 0;
	int afterCount = 0;
	String beforeText = "";
	TextWatcher onEditTextChanged = new TextWatcher(){
		public void afterTextChanged(Editable s) {
			//beforeText = s.toString();
			//Toast.makeText(QwertyActivity.this, "count : " + s, Toast.LENGTH_SHORT).show();
		}
		public void beforeTextChanged(CharSequence s, int start, int count, int after){
			beforeText = s.toString();
			//Toast.makeText(QwertyActivity.this, "count : " + s, Toast.LENGTH_SHORT).show();
			
		}

		public void onTextChanged(CharSequence s, int start, int before, int count){
			Toast.makeText(QwertyActivity.this, "start : " + start, Toast.LENGTH_SHORT).show();
			beforeCount = beforeText.length();
			afterCount = s.length();
			if(afterCount < beforeCount) {
				Action.BACKSPACE.execute(getCommands());
			} else {
				CharSequence c = s.subSequence(start, start+1);
				handleChar(c.toString());
			}
		}
	};

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
		Toast.makeText(QwertyActivity.this, "완료", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onTrackballEvent(event);
	}

	/*
	 * @Override public boolean onKeyDown(int keyCode, KeyEvent event) { if
	 * (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { switch
	 * (event.getAction()) { case KeyEvent.ACTION_DOWN: Toast.makeText(this,
	 * "x", Toast.LENGTH_SHORT).show(); finish(); return true; } } return false;
	 * }
	 */

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig);
	 * 
	 * // Checks whether a hardware keyboard is available if
	 * (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
	 * Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show(); }
	 * else if (newConfig.hardKeyboardHidden ==
	 * Configuration.HARDKEYBOARDHIDDEN_YES) { Toast.makeText(this,
	 * "keyboard hidden", Toast.LENGTH_SHORT).show(); } }
	 */

	/*
	 * public boolean onKeyPreIme(int keyCode, KeyEvent event) { if
	 * (event.getKeyCode() == KeyEvent.KEYCODE_BACK) { finish(); return true; //
	 * So it is not propagated. } return super.dispatchKeyEvent(event); }
	 */

}
