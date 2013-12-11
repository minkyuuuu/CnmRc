package com.cnm.cnmrc.global;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.R.raw;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SoundEffect {
	private SoundPool mSoundPool;
	private AudioManager mAudioManager;
	private int soundEffect;
	private float mVolume = 1f;
	Context context;
	CnmPreferences pref;

	public SoundEffect(Context context, CnmPreferences pref) {
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundEffect = mSoundPool.load(context, R.raw.button_click_sound, 1);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		this.context = context;
		this.pref = pref;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		mSoundPool.release();
	}

	private void soundEffectCheck() {
		switch (mAudioManager.getRingerMode()) {
		case AudioManager.RINGER_MODE_NORMAL:
			mVolume = 1f;
			break;
		case AudioManager.RINGER_MODE_SILENT:
			mVolume = 0f;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			mVolume = 0f;
			break;
		default:
			break;
		}

	}

	public void play() {
		if (pref.loadConfigSoundEffect(context)) {
			Log.d("hwang", "sound paly");
			
			//soundEffectCheck();
			mSoundPool.play(soundEffect, mVolume, mVolume, 0, 0, 1f);
		}
	}
}
