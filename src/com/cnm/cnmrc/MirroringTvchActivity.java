package com.cnm.cnmrc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MirroringTvchActivity extends FragmentActivity implements View.OnClickListener {

	LinearLayout tvchLayout;
	RelativeLayout tvchTopLayout;
	RelativeLayout tvchMidChLayout;
	LinearLayout tvchMidVolLayout;
	LinearLayout tvchBottomLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// change to Full screen (No status bar & No title bar)
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN); // OK

		setContentView(R.layout.activity_mirroring_tvch);

		// tvch
		tvchLayout = (LinearLayout) findViewById(R.id.mirroring_tvch);
		
		// block dispatching of touch envent
		tvchTopLayout = (RelativeLayout) findViewById(R.id.mirroring_tvch_top);
		tvchTopLayout.setOnClickListener(this);
		tvchMidChLayout = (RelativeLayout) findViewById(R.id.mirroring_tvch_mid_ch);
		tvchMidChLayout.setOnClickListener(this);
		tvchMidVolLayout = (LinearLayout) findViewById(R.id.mirroring_tvch_mid_vol);
		tvchMidVolLayout.setOnClickListener(this);
		tvchBottomLayout = (LinearLayout) findViewById(R.id.mirroring_tvch_bottom);
		tvchBottomLayout.setOnClickListener(this);
		
		Button exit = (Button) findViewById(R.id.mirroring_exit);
		exit.setOnClickListener(this);

		ImageButton vod = (ImageButton) findViewById(R.id.mirroring_tvch_icon);
		vod.setOnClickListener(this);

		ImageButton mute = (ImageButton) findViewById(R.id.mirroring_tvch_volmute);
		mute.setOnClickListener(this);

		// change to landscape mode
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// // OK
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //
		// OK
		
		hideTvch();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// invalidate();
			if(tvchTopLayout.getVisibility() == View.INVISIBLE) showTvch();
			else hideTvch();
			
			Log.i("hwang", "tvch activity is touching!!!");
			break;
		}
		return true;
		//return super.onTouchEvent(event);
	}
	
	private void showTvch() {
		tvchTopLayout.setVisibility(View.VISIBLE);
		tvchMidChLayout.setVisibility(View.VISIBLE);
		tvchMidVolLayout.setVisibility(View.VISIBLE);
		tvchBottomLayout.setVisibility(View.VISIBLE);
	}
	
	private void hideTvch() {
		tvchTopLayout.setVisibility(View.INVISIBLE);
		tvchMidChLayout.setVisibility(View.INVISIBLE);
		tvchMidVolLayout.setVisibility(View.INVISIBLE);
		tvchBottomLayout.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onBackPressed() {
		finish();

		// FragmentTransaction ft =
		// getSupportFragmentManager().beginTransaction();
		// PopupMirroringExit mirroringExit = new PopupMirroringExit();
		// mirroringExit.show(ft, PopupMirroringExit.class.getSimpleName());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mirroring_exit:
			onBackPressed();
			break;
		case R.id.mirroring_tvch_icon:
			finish();
			Intent intent = new Intent(this, MirroringVodActivity.class);
			startActivity(intent);
			break;
		case R.id.mirroring_tvch_volmute:
			if (!v.isSelected())
				v.setSelected(true);
			else
				v.setSelected(false);
			break;
		}
	}

}
