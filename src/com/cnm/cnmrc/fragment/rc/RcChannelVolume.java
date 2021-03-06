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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.ExtendedImageButton;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.util.Action;

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
	ExtendedImageButton mVolPlus, mVolMinus, mMute; // volume
	ImageView mAnimVolPlus, mAnimMute, mAnimVolMinus;

	ExtendedImageButton mChUp, mChDown; 			// channel
	ImageButton mChname; 							// channel
	ImageView mAnimChUp, mAnimChDown;
	
	ExtendedImageButton mOK;
	ImageView mAnimOK;

//	boolean mToogleMuteOff = true;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_channel_volume, container, false);

		mVolPlus = (ExtendedImageButton) layout.findViewById(R.id.control_volplus);
		mAnimVolPlus = (ImageView) layout.findViewById(R.id.anim_volplus);
		mVolPlus.setOnClickListener(this);

		mMute = (ExtendedImageButton) layout.findViewById(R.id.control_mute);
//		mAnimMute = (ImageView) layout.findViewById(R.id.anim_mute);
		//mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute off
//		mToogleMuteOff = true;
		mMute.setOnClickListener(this);

		mVolMinus = (ExtendedImageButton) layout.findViewById(R.id.control_volminus);
		mAnimVolMinus = (ImageView) layout.findViewById(R.id.anim_volminus);
		mVolMinus.setOnClickListener(this);

		mChUp = (ExtendedImageButton) layout.findViewById(R.id.control_chup);
		mAnimChUp = (ImageView) layout.findViewById(R.id.anim_chup);
		mChUp.setOnClickListener(this);

		mChDown = (ExtendedImageButton) layout.findViewById(R.id.control_chdown);
		mAnimChDown = (ImageView) layout.findViewById(R.id.anim_chdown);
		mChDown.setOnClickListener(this);

		mChname = (ImageButton) layout.findViewById(R.id.panel_chname);
		mChname.setOnClickListener(this);
		
		mOK = (ExtendedImageButton) layout.findViewById(R.id.ok);
		mAnimOK = (ImageView) layout.findViewById(R.id.anim_ok);
		mOK.setOnClickListener(this);

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
			if (!oneClickTapPress) return;
			Action.VOLUME_UP.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolPlus);
//			mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute 배경이미지가 바뀌어야 한다.
//			mToogleMuteOff = true;
			break;
		case R.id.control_mute:
			if (!oneClickTapPress) return;
			Action.MUTE.execute(((BaseActivity)getActivity()).getCommands());
			//oneClickTapPress = false;
			
//			startLoadingAni((ImageButton) v, mAnimMute);
//			if (mToogleMuteOff) {
//				mMute.setBackgroundResource(R.drawable.xml_control_mute_on); // mute 배경이미지가 바뀌어야 한다.
//				mToogleMuteOff = false;
//			} else {
//				mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute 배경이미지가 바뀌어야 한다.
//				mToogleMuteOff = true;
//			}
			break;
		case R.id.control_volminus:
			if (!oneClickTapPress) return;
			Action.VOLUME_DOWN.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimVolMinus);
			mMute.setBackgroundResource(R.drawable.xml_control_mute_off); // mute 배경이미지가 바뀌어야 한다.
//			mToogleMuteOff = true;
			break;
		case R.id.control_chup:
			if (!oneClickTapPress) return;
			Action.CHANNEL_UP.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChUp);
			break;
		case R.id.control_chdown:
			if (!oneClickTapPress) return;
			Action.CHANNEL_DOWN.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimChDown);
			break;
		case R.id.ok:
			if (!oneClickTapPress) return;
			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, mAnimOK);
			break;
		}
	}


}
