package com.cnm.cnmrc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.custom.ExtendedEditText;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.TouchHandlerA;
import com.google.android.apps.tvremote.TouchHandlerA.Mode;
import com.google.android.apps.tvremote.util.Action;

public class QwertyActivityTest extends BaseActivity {

	ExtendedEditText edit;
	Button mQwertyCancel, mQwertyTextClear;
	private TextInputHandler textInputHandler = null;

	/**
	 * The main view.
	 */
	private View view;

	public QwertyActivityTest() {
		textInputHandler = new TextInputHandler(this, getCommands());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qwerty);

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		
		edit = (ExtendedEditText) findViewById(R.id.qwerty_edit);
		edit.setFocusable(true);
		edit.setFocusableInTouchMode(true);
		edit.requestFocus();
		edit.setPrivateImeOptions("defaultInputmode=english;symbol=false");

		//		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		//			@Override
		//			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		//				if (actionId == EditorInfo.IME_ACTION_DONE) {
		//
		//					String str = v.getText().toString();
		//					if (str.equals("")) {
		//						Toast.makeText(QwertyActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
		//					} else {
		//						performDone();
		//					}
		//
		//					return true;
		//				}
		//				return false;
		//			}
		//
		//		});

		//		edit.setOnKeyListener(new OnKeyListener() {
		//			@Override
		//			public boolean onKey(View v, int keyCode, KeyEvent event) {
		//				if (keyCode == KeyEvent.KEYCODE_ENTER) {
		//
		//				}
		//				return false;
		//			}
		//		});

		((ExtendedEditText) edit).setInterceptor(new ExtendedEditText.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivityTest.this.onUserInteraction();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK:
						finish();
						return true;

					case KeyEvent.KEYCODE_SEARCH:
						Action.NAVBAR.execute(getCommands());
						return true;

					case KeyEvent.KEYCODE_ENTER:
						Action.ENTER.execute(getCommands());
						finish();
						return true;
					case KeyEvent.KEYCODE_DEL:
						Action.BACKSPACE.execute(getCommands());
						return true;
					}
				}

				return textInputHandler.handleKey(event);
			}

			public boolean onSymbol(char c) {
				QwertyActivityTest.this.onUserInteraction();
				textInputHandler.handleChar(c);
				//handleChar(c);
				return false;
			}
		});
		
		// hwang 2013-12-01
		//textInputHandler.setDisplay((TextView) findViewById(R.id.text_feedback_chars));

		// Attach touch handler to the touch pad.
		view = (View) findViewById(R.id.view);
		new TouchHandlerA(view, Mode.POINTER_MULTITOUCH, getCommands());

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
		Toast.makeText(QwertyActivityTest.this, "완료", Toast.LENGTH_SHORT).show();

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
