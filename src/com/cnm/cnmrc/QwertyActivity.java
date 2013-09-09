package com.cnm.cnmrc;

import com.cnm.cnmrc.popup.PopupSearchRecentlyDelete;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QwertyActivity extends FragmentActivity implements View.OnClickListener {

	EditText edit;
	Button mQwertyCancel, mQwertyTextClear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_qwerty);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
		
		mQwertyCancel = (Button) findViewById(R.id.qwerty_cancel);
		mQwertyCancel.setOnClickListener(this);
		
		mQwertyTextClear = (Button) findViewById(R.id.qwerty_textclear);
		mQwertyTextClear.setOnClickListener(this);

	}

	private void performDone() {
		Toast.makeText(QwertyActivity.this, "완료", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qwerty_cancel:
			finish();
			break;
		case R.id.qwerty_textclear:
			edit.setText("");
			Toast.makeText(QwertyActivity.this, "TV글자 지우기", Toast.LENGTH_SHORT).show();
			break;
		}

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
