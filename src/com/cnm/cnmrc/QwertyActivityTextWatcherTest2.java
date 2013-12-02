package com.cnm.cnmrc;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.cnm.cnmrc.custom.ExtendedEditTextTextWatcherTest2;
import com.cnm.cnmrc.util.CnmPreferences;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TextInputHandler;
import com.google.android.apps.tvremote.TouchHandler;
import com.google.android.apps.tvremote.TouchHandler.Mode;
import com.google.android.apps.tvremote.util.Action;

public class QwertyActivityTextWatcherTest2 extends BaseActivity {

	ExtendedEditTextTextWatcherTest2 edit;
	Button mQwertyCancel, mQwertyTextClear;
	private TextInputHandler textInputHandler = null;

	/**
	 * The main view.
	 */
	private View view;

	public QwertyActivityTextWatcherTest2() {
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
		
		String locale1 = this.getResources().getConfiguration().locale.getDisplayName();	// 한국어, English
		Log.v("hwang2", locale1);
		String locale2 = java.util.Locale.getDefault().getDisplayName();
		Log.v("hwang2", locale2);

		edit = (ExtendedEditTextTextWatcherTest2) findViewById(R.id.qwerty_edit);
		//edit.setPrivateImeOptions("defaultInputmode=english"); // not working

		((ExtendedEditTextTextWatcherTest2) edit).setInterceptor(new ExtendedEditTextTextWatcherTest2.Interceptor() {
			public boolean onKeyEvent(KeyEvent event) {
				QwertyActivityTextWatcherTest2.this.onUserInteraction();
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

		// Attach touch handler to the touch pad.
		view = (View) findViewById(R.id.view);
		new TouchHandler(view, Mode.POINTER_MULTITOUCH, getCommands());

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

	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			finish();
		}
		return super.onTrackballEvent(event);
	}

}
