package com.cnm.cnmrc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cnm.cnmrc.fragment.popup.PopupMirroringExit;

public class MirroringActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// change to Full screen (No status bar & No title bar)
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);	// OK

		setContentView(R.layout.activity_mirroring);

		// change to landscape mode
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	// OK
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 	// OK

	}

	@Override
	public void onBackPressed() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		PopupMirroringExit mirroringExit = new PopupMirroringExit();
		mirroringExit.show(ft, PopupMirroringExit.class.getSimpleName());
	}

}
