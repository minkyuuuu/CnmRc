package com.cnm.cnmrc;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cnm.cnmrc.custom.ExtendedEditText;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.util.Action;

public class QwertyActivity extends BaseActivity {

	private TextInputHandler textInputHandler = null;
	private ExtendedEditText editText;
	private View view;

	public QwertyActivity() {
		textInputHandler = new TextInputHandler(this, getCommands());
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

		// 폰은 한글 기준이고, 셋탑은 영문기준이다.  그러나 폰은 don't care일것 같다.
		String locale = this.getResources().getConfiguration().locale.getDisplayName(); // 한국어, English
		//String locale = java.util.Locale.getDefault().getDisplayName();
		//Log.v("hwang2", locale);
		//CharSequence status = "English";
		//if(locale.contains(status)) textInputHandler.setStatusApp(true);
		//else textInputHandler.setStatusApp(false);
		
		editText = (ExtendedEditText) findViewById(R.id.qwerty_edit);
		editText.requestFocus();
		((ExtendedEditText) editText).setInterceptor(new ExtendedEditText.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivity.this.onUserInteraction();
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (event.getKeyCode()) {
					case KeyEvent.KEYCODE_BACK:
						finish();
						return true;

					case KeyEvent.KEYCODE_ENTER:
						Action.ENTER.execute(getCommands());
						finish();
						return true;
					}
				}
				return textInputHandler.handleKey(event);
			}

			public boolean onSymbol(char c) {
				QwertyActivity.this.onUserInteraction();
				textInputHandler.handleChar(c);
				return false;
			}
			
			public boolean onSymbolA(char c) {
				QwertyActivity.this.onUserInteraction();
				//view.getText().clear();
				//view.getText().clearSpans();
				//TextKeyListener.clear(view.getText());
				editText.setText(null);
				textInputHandler.handleCharA(c);
				return false;
			}
		});

		textInputHandler.setDisplay((TextView) findViewById(R.id.text_feedback_chars));

		// Attach touch handler to the touch pad.
		view = (View) findViewById(R.id.view);
		//new TouchHandler(view, Mode.POINTER_MULTITOUCH, getCommands());
	}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onTrackballEvent(event);
	}

}
