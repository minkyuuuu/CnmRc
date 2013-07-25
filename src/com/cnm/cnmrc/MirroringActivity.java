package com.cnm.cnmrc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cnm.cnmrc.fragment.popup.MirroringEnterPopup;
import com.cnm.cnmrc.fragment.popup.MirroringExitPopup;

public class MirroringActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mirroring);
		
		// change to landscape mode
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
	}
	
	@Override
	public void onBackPressed() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		MirroringExitPopup mirroringExit = new MirroringExitPopup();
		//ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		//ft.setCustomAnimations(R.anim.zoom_enter, 0);
		mirroringExit.show(ft, MirroringExitPopup.class.getSimpleName());
		//super.onBackPressed();
	}

	
}
