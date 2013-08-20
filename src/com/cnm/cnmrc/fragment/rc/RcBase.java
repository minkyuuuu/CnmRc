/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnm.cnmrc.fragment.rc;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.rc.RcBottomMenu;

public class RcBase extends Fragment {

	AnimationDrawable mTapPressAnimation;
	int mTapPressDuration = 0;
	Handler mHandler = new Handler();
	
	boolean oneClickTapPress = true;
    
	/**
	 * Loading TapPressAnimation 시작<br>
	 */
	protected void startLoadingAni(ImageButton view, final ImageView animView) {
		if (view != null) {
			view.post(new Runnable() {
				@Override
				public void run() {
					try {
						mTapPressAnimation = new AnimationDrawable();
						mTapPressAnimation.addFrame(getResources().getDrawable(R.drawable.tappress01), 60);
						mTapPressAnimation.addFrame(getResources().getDrawable(R.drawable.tappress02), 60);
						mTapPressAnimation.addFrame(getResources().getDrawable(R.drawable.tappress03), 60);
						mTapPressAnimation.addFrame(getResources().getDrawable(R.drawable.tappress04), 60);
						mTapPressAnimation.setOneShot(true);
						animView.setImageDrawable(mTapPressAnimation);

						// API 2.3.4에서는 잘 안된다... 왜지???
						// animView.setBackgroundResource(R.drawable.anim_tappress);
						// mTapPressAnimation = (AnimationDrawable)
						// animView.getBackground();

						mTapPressDuration = 0;
						for (int i = 0; i < mTapPressAnimation.getNumberOfFrames(); i++) {
							mTapPressDuration += mTapPressAnimation.getDuration(i);
						}

						animView.setVisibility(View.VISIBLE);

						// Start the animation (looped playback by default).
						mTapPressAnimation.start();

						mHandler.postDelayed(new Runnable() {
							public void run() {
								mTapPressAnimation.stop();
								mTapPressAnimation = null;
								animView.setBackgroundResource(0);
								animView.setVisibility(View.INVISIBLE);
								oneClickTapPress = true;
							}
						}, mTapPressDuration);
					} catch (Exception e) {
						e.getStackTrace();
					}

				}
			});
		}
		
		//view.post(new Starter());
	}
	
	class Starter implements Runnable {
		@Override
		public void run() {
			mTapPressAnimation.start();
		}
	}
	

}
