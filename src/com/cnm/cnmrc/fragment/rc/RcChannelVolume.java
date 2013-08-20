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

public class RcChannelVolume extends RcBase implements View.OnClickListener {
	
	public static RcChannelVolume newInstance(String type) {
		RcChannelVolume f = new RcChannelVolume();
		Bundle args = new Bundle();
		args.putString("type", type);
		f.setArguments(args);
		return f;
	}
	
	

	View layout;

	AnimationDrawable mTapPressAnimation;
	ImageButton mVolPlus, mVolMinus, mMute; // volume
	ImageView mAnimVolPlus, mAnimMute, mAnimVolMinus;

	ImageButton mChUp, mChDown, mChname; // channel
	ImageView mAnimChUp, mAnimChDown;

	boolean mToogleMuteOff = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_channel_volume, container, false);

		mVolPlus = (ImageButton) layout.findViewById(R.id.control_volplus);
		mAnimVolPlus = (ImageView) layout.findViewById(R.id.anim_volplus);
		mVolPlus.setOnClickListener(this);

		mMute = (ImageButton) layout.findViewById(R.id.control_mute);
		mAnimMute = (ImageView) layout.findViewById(R.id.anim_mute);
		mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute off
		mToogleMuteOff = true;
		mMute.setOnClickListener(this);

		mVolMinus = (ImageButton) layout.findViewById(R.id.control_volminus);
		mAnimVolMinus = (ImageView) layout.findViewById(R.id.anim_volminus);
		mVolMinus.setOnClickListener(this);

		mChUp = (ImageButton) layout.findViewById(R.id.control_chup);
		mAnimChUp = (ImageView) layout.findViewById(R.id.anim_chup);
		mChUp.setOnClickListener(this);

		mChDown = (ImageButton) layout.findViewById(R.id.control_chdown);
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

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.control_volplus:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolPlus);
			break;
		case R.id.control_mute:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			
			startLoadingAni((ImageButton) v, mAnimMute);
			if (mToogleMuteOff) {
				mMute.setBackgroundResource(R.drawable.xml_control_mute_on); // mute 배경이미지가 바뀌어야 한다.
				mToogleMuteOff = false;
			} else {
				mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute 배경이미지가 바뀌어야 한다.
				mToogleMuteOff = true;
			}
			break;
		case R.id.control_volminus:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolMinus);
			break;
		case R.id.control_chup:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChUp);
			break;
		case R.id.control_chdown:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChDown);
			break;
		}
	}


}
