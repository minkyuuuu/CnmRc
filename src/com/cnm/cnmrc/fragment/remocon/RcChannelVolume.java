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

package com.cnm.cnmrc.fragment.remocon;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cnm.cnmrc.R;

public class RcChannelVolume extends Fragment implements View.OnClickListener {

	View layout;
	
	AnimationDrawable mFrameAnimation;
	ImageButton mVolPlus, mVolMinus; // panel

	public static RcChannelVolume newInstance(long num) {
		RcChannelVolume f = new RcChannelVolume();
		Bundle args = new Bundle();
		args.putLong("num", num);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_channel_volume, container, false);

		mVolPlus = (ImageButton) layout.findViewById(R.id.panel_volplus);
		mVolPlus.setOnClickListener(this);

		mVolMinus = (ImageButton) layout.findViewById(R.id.panel_volminus);
		mVolMinus.setOnClickListener(this);

		return layout;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.panel_volplus:
			// Load the ImageView that will host the animation and
			// set its background to our AnimationDrawable XML resource.
			mVolPlus.setBackgroundResource(R.drawable.anim_tappress);

			// Get the background, which has been compiled to an
			// AnimationDrawable object.
			mFrameAnimation = (AnimationDrawable) mVolPlus.getBackground();
			mVolPlus.getBackground();

			// Start the animation (looped playback by default).
			mFrameAnimation.start();
			Log.i("hwang", "panel_volplus");
			break;
			
		case R.id.panel_volminus:
			mFrameAnimation.stop();
			Log.i("hwang", "panel_volplus");
			break;
		}
	}

}
