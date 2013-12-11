package com.cnm.cnmrc.global;

import com.cnm.cnmrc.CnmPreferences;

import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

public class VibratorEffect {
	private Vibrator mVibrator;
	
	Context context;
	CnmPreferences pref;

	public VibratorEffect(Context context, CnmPreferences pref) {
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		this.context = context;
		this.pref = pref;
	}

	public void play() {
		if (pref.loadConfigVibrateEffect(context)) {
			Log.d("hwang", "vibrator paly");
			
			//long[] pattern = {1000, 200, 1000, 2000, 1200};      	// 진동, 무진동, 진동 무진동 숫으로 시간을 설정한다.
			//mVibrator.vibrate(pattern, 0);						// 패턴을 지정하고 반복횟수를 지정
			mVibrator.vibrate(300);                                //1초 동안 진동이 울린다.
		}
	}
}
