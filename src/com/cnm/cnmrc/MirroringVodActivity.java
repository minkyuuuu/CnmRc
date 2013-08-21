package com.cnm.cnmrc;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MirroringVodActivity extends FragmentActivity implements View.OnClickListener {
	
	LinearLayout vodLayout;
	RelativeLayout vodTopLayout;
	LinearLayout vodMidVolLayout;
	RelativeLayout vodMidSpeedLayout;
	RelativeLayout vodBottomLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mirroring_vod);
		
		// vod
		vodLayout = (LinearLayout) findViewById(R.id.mirroring_vod);
		
		// block dispatching of touch envent
		vodTopLayout = (RelativeLayout) findViewById(R.id.mirroring_vod_top);
		vodTopLayout.setOnClickListener(this);
		vodMidVolLayout = (LinearLayout) findViewById(R.id.mirroring_tvch_mid_vol);
		vodMidVolLayout.setOnClickListener(this);
		vodMidSpeedLayout = (RelativeLayout) findViewById(R.id.mirroring_vod_mid_speed);
		vodMidSpeedLayout.setOnClickListener(this);
		vodBottomLayout = (RelativeLayout) findViewById(R.id.mirroring_vod_bottom);
		vodBottomLayout.setOnClickListener(this);
		
		Button exit = (Button) findViewById(R.id.mirroring_exit);
		exit.setOnClickListener(this);
		
		ImageButton vod = (ImageButton) findViewById(R.id.mirroring_vod_icon);
		vod.setOnClickListener(this);
		
		ImageButton vodVol = (ImageButton) findViewById(R.id.mirroring_vod_vol);
		vodVol.setOnClickListener(this);
		
		ImageButton pause = (ImageButton) findViewById(R.id.mirroring_vod_pause);
		pause.setOnClickListener(this);
		
		
		// test
		Intent intent = getIntent();
		boolean isChaeWon = intent.getBooleanExtra("isChaeWon1", true);
		if(isChaeWon) {
			vodLayout.setBackgroundResource(R.drawable.moon_chaewon1);
		} else {
			vodLayout.setBackgroundResource(R.drawable.moon_chaewon2);
		}


		hideVod();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// invalidate();
			if(vodTopLayout.getVisibility() == View.INVISIBLE) showVod();
			else hideVod();
			
			Log.i("hwang", "vod activity is touching!!!");
			break;
		}
		return true;
		//return super.onTouchEvent(event);
	}
	
	private void showVod() {
		vodTopLayout.setVisibility(View.VISIBLE);
		vodMidVolLayout.setVisibility(View.INVISIBLE);
		vodMidSpeedLayout.setVisibility(View.VISIBLE);
		vodBottomLayout.setVisibility(View.VISIBLE);
	}
	
	private void hideVod() {
		vodTopLayout.setVisibility(View.INVISIBLE);
		vodMidVolLayout.setVisibility(View.INVISIBLE);
		vodMidSpeedLayout.setVisibility(View.INVISIBLE);
		vodBottomLayout.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onBackPressed() {
		finish();
		
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		PopupMirroringExit mirroringExit = new PopupMirroringExit();
//		mirroringExit.show(ft, PopupMirroringExit.class.getSimpleName());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mirroring_vod_vol:
			if(vodMidVolLayout.getVisibility() == View.INVISIBLE) vodMidVolLayout.setVisibility(View.VISIBLE);
			else vodMidVolLayout.setVisibility(View.INVISIBLE);
			break;
		case R.id.mirroring_vod_pause:
			if(!v.isSelected()) v.setSelected(true);
			else v.setSelected(false);
			break;
		case R.id.mirroring_exit:
			onBackPressed();
			break;
		case R.id.mirroring_vod_icon:
			LinearLayout mirroringVod = (LinearLayout) findViewById(R.id.mirroring_vod);
			Drawable currentDrawable = mirroringVod.getBackground();
			Drawable drawable = getResources().getDrawable(R.drawable.moon_chaewon1);
			
			// drawable type compare 5 - OK
			if(currentDrawable.getConstantState().equals(drawable.getConstantState())) {
				Log.i("hwang", "compare drawable --> equal");
				mirroringVod.setBackgroundResource(R.drawable.moon_chaewon2);
			} else {
				Log.i("hwang", "compare drawable --> not equal");
				mirroringVod.setBackgroundResource(R.drawable.moon_chaewon1);
			}
			
			// drawable type compare 4 - OK
//			Bitmap bitmap1 = ((BitmapDrawable)currentDrawable).getBitmap();
//			Bitmap bitmap2 = ((BitmapDrawable)drawable).getBitmap();
//
//			if(bitmap1 == bitmap2) {
//				Log.i("hwang", "compare drawable --> equal");
//				mirroringVod.setBackgroundResource(R.drawable.moon_chaewon2);
//			} else {
//				Log.i("hwang", "compare drawable --> not equal");
//				mirroringVod.setBackgroundResource(R.drawable.moon_chaewon1);
//			}
			
			
			break;
		}
	}
	


}
