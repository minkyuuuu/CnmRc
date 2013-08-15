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

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.cnm.cnmrc.R;

public class RcChannelVolume extends Fragment implements View.OnClickListener {

	View layout;

	AnimationDrawable mTapPressAnimation;
	ImageButton mVolPlus, mVolMinus, mMute; // volume
	ImageView mAnimVolPlus, mAnimMute, mAnimVolMinus;

	ImageButton mChUp, mChDown, mChname; // channel
	ImageView mAnimChUp, mAnimChDown;



	boolean mToogleMuteOff = true;

	public static RcChannelVolume newInstance(long num) {
		RcChannelVolume f = new RcChannelVolume();
		Bundle args = new Bundle();
		args.putLong("num", num);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_channel_volume, container, false);

		mVolPlus = (ImageButton) layout.findViewById(R.id.panel_volplus);
		mAnimVolPlus = (ImageView) layout.findViewById(R.id.anim_volplus);
		mVolPlus.setOnClickListener(this);

		mMute = (ImageButton) layout.findViewById(R.id.panel_mute);
		mAnimMute = (ImageView) layout.findViewById(R.id.anim_mute);
		mMute.setBackgroundResource(R.drawable.xml_panel_mute_off); // mute off
		mToogleMuteOff = true;
		mMute.setOnClickListener(this);

		mVolMinus = (ImageButton) layout.findViewById(R.id.panel_volminus);
		mAnimVolMinus = (ImageView) layout.findViewById(R.id.anim_volminus);
		mVolMinus.setOnClickListener(this);

		mChUp = (ImageButton) layout.findViewById(R.id.panel_chup);
		mAnimChUp = (ImageView) layout.findViewById(R.id.anim_chup);
		mChUp.setOnClickListener(this);

		mChDown = (ImageButton) layout.findViewById(R.id.panel_chdown);
		mAnimChDown = (ImageView) layout.findViewById(R.id.anim_chdown);
		mChDown.setOnClickListener(this);

		mChname = (ImageButton) layout.findViewById(R.id.panel_chname);
		mChname.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		/*
		 * mTapPressAnimation = (AnimationDrawable)
		 * mAnimVolPlus.getBackground(); for (int i = 0; i <
		 * mTapPressAnimation.getNumberOfFrames(); i++) { mTapPressDuration +=
		 * mTapPressAnimation.getDuration(i); }
		 */

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	Handler mHandler = new Handler();
	int mTapPressDuration = 0;
	boolean oneClickTapPress = true;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.panel_volplus:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolPlus);
			break;
		case R.id.panel_mute:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			
			startLoadingAni((ImageButton) v, mAnimMute);
			if (mToogleMuteOff) {
				mMute.setBackgroundResource(R.drawable.xml_panel_mute_on); // mute 배경이미지가 바뀌어야 한다.
				mToogleMuteOff = false;
			} else {
				mMute.setBackgroundResource(R.drawable.xml_panel_mute_off); // mute 배경이미지가 바뀌어야 한다.
				mToogleMuteOff = true;
			}
			break;
		case R.id.panel_volminus:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolMinus);
			break;
		case R.id.panel_chup:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChUp);
			break;
		case R.id.panel_chdown:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChDown);
			break;
		}
	}

	/**
	 * Loading TapPressAnimation 시작<br>
	 */
	private void startLoadingAni(ImageButton view, final ImageView animView) {
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
