package com.cnm.cnmrc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

public class QwertyActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_qwerty);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
	}
	
}
