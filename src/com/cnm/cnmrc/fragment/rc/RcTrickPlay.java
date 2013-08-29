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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cnm.cnmrc.R;

public class RcTrickPlay extends RcBase implements View.OnClickListener {

	View layout;

	ImageButton play, ff, stop, rew, pause;
	ImageView aniPlay, aniFF, aniStop, aniRew, aniPause;

	public static RcTrickPlay newInstance(String type) {
		RcTrickPlay f = new RcTrickPlay();
		Bundle args = new Bundle();
		args.putString("type", type);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_trick_play, container, false);

		// 1) play
		play = (ImageButton) layout.findViewById(R.id.trick_play);
		aniPlay = (ImageView) layout.findViewById(R.id.anim_trick_play);
		play.setOnClickListener(this);

		//2) ff
		ff = (ImageButton) layout.findViewById(R.id.trick_ff);
		aniFF = (ImageView) layout.findViewById(R.id.anim_trick_ff);
		ff.setOnClickListener(this);
		
		// 3) stop
		stop = (ImageButton) layout.findViewById(R.id.trick_stop);
		aniStop = (ImageView) layout.findViewById(R.id.anim_trick_stop);
		stop.setOnClickListener(this);
		
		// 4) rew
		rew = (ImageButton) layout.findViewById(R.id.trick_rew);
		aniRew = (ImageView) layout.findViewById(R.id.anim_trick_rew);
		rew.setOnClickListener(this);

		// 5) pause
		pause = (ImageButton) layout.findViewById(R.id.trick_pause);
		aniPause = (ImageView) layout.findViewById(R.id.anim_trick_pause);
		pause.setOnClickListener(this);

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
		case R.id.trick_play:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniPlay);
			break;
		case R.id.trick_ff:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniFF);
			break;
		case R.id.trick_stop:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniStop);
			break;
		case R.id.trick_rew:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniRew);
			break;
		case R.id.trick_pause:
			if (!oneClickTapPress)
				return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniPause);
			break;
		}
	}

}