package com.cnm.cnmrc.util;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.R.raw;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class Sound {
	private SoundPool mSoundPool;
	private AudioManager mAudioManager;
	private int tak;
	private float mVolume = 1f;

	public Sound(Context context) {
		mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		tak = mSoundPool.load(context, R.raw.button_click_sound, 1);
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
		Log.d("hwang", "paly");

		soundEffectCheck();
		mSoundPool.play(tak, mVolume, mVolume, 0, 0, 1f);

	}
}
