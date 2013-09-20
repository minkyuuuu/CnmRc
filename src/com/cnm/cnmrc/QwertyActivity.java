package com.cnm.cnmrc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.TouchHandler;
import com.google.android.apps.tvremote.TouchHandler.Mode;
import com.google.android.apps.tvremote.util.Action;
import com.google.android.apps.tvremote.widget.ImeInterceptView;

public class QwertyActivity extends BaseActivity implements View.OnClickListener {

	EditText edit;
	Button mQwertyCancel, mQwertyTextClear;

	/**
	 * Captures text inputs.
	 */
	private final TextInputHandler textInputHandler;

	/**
	 * The main view.
	 */
	private ImeInterceptView view;

	public QwertyActivity() {
		textInputHandler = new TextInputHandler(this, getCommands());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qwerty);

		//getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		//
		edit = (EditText) findViewById(R.id.qwerty_edit);
		edit.requestFocus();

		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {

					String str = v.getText().toString();
					if (str.equals("")) {
						Toast.makeText(QwertyActivity.this, "입력해 주세요", Toast.LENGTH_SHORT).show();
					} else {
						performDone();
					}

					return true;
				}
				return false;
			}

		});

		view = (ImeInterceptView) findViewById(R.id.keyboard);
		//view.requestFocus();
		view.setInterceptor(new ImeInterceptView.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivity.this.onUserInteraction();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (event.getKeyCode()) {
//					case KeyEvent.KEYCODE_DEL:
//						if(edit.length() > 0) {
//							edit.setText(edit.getText().delete(edit.length() - 1, edit.length()));
//						}
//						return false;
						
					case KeyEvent.KEYCODE_BACK:
						finish();
						return true;

					case KeyEvent.KEYCODE_SEARCH:
						Action.NAVBAR.execute(getCommands());
						return true;

					case KeyEvent.KEYCODE_ENTER:
						Action.ENTER.execute(getCommands());
						//finish();
						return true;
					}
				}
				return textInputHandler.handleKey(event);
			}

			public boolean onSymbol(char c) {
				QwertyActivity.this.onUserInteraction();
				textInputHandler.handleChar(c);
				//edit.setText(appendDisplayedText(String.valueOf(c)));
				return false;
			}
		});

		textInputHandler.setDisplay((TextView) findViewById(R.id.text_feedback_chars));

		// Attach touch handler to the touch pad.
		new TouchHandler(view, Mode.POINTER_MULTITOUCH, getCommands());

	}

	private String appendDisplayedText(CharSequence seq) {
		seq = new StringBuffer(edit.getText()).append(seq);
		return String.valueOf(seq);
	}

	private void performDone() {
		Toast.makeText(QwertyActivity.this, "완료", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

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
