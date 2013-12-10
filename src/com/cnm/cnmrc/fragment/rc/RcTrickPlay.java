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
import android.widget.RelativeLayout;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.TouchHandler;
import com.google.android.apps.tvremote.TouchHandler.Mode;
import com.google.android.apps.tvremote.util.Action;

public class RcTrickPlay extends RcBase implements View.OnClickListener {

	View layout;

	ImageButton play, ff, stop, rew, pause;
	ImageView aniPlay, aniFF, aniStop, aniRew, aniPause;
	
	RelativeLayout view;	// Attach touch handler to the touch pad.

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
		ff = (ImageButton) layout.findViewById(R.id.tricky_ff);
		aniFF = (ImageView) layout.findViewById(R.id.anim_tricky_ff);
		ff.setOnClickListener(this);
		
		// 3) stop
		stop = (ImageButton) layout.findViewById(R.id.tricky_stop);
		aniStop = (ImageView) layout.findViewById(R.id.anim_tricky_stop);
		stop.setOnClickListener(this);
		
		// 4) rew
		rew = (ImageButton) layout.findViewById(R.id.tricky_rew);
		aniRew = (ImageView) layout.findViewById(R.id.anim_tricky_rew);
		rew.setOnClickListener(this);

		// 5) pause
		pause = (ImageButton) layout.findViewById(R.id.tricky_pause);
		aniPause = (ImageView) layout.findViewById(R.id.anim_tricky_pause);
		pause.setOnClickListener(this);
		
		view = (RelativeLayout) layout.findViewById(R.id.view);
		//new TouchHandler(view, Mode.POINTER_MULTITOUCH, ((BaseActivity)getActivity()).getCommands(), getActivity());
		
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
			if (!oneClickTapPress) return;
			Action.MEDIA_PLAY.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniPlay);
			break;
		case R.id.tricky_ff:
			if (!oneClickTapPress) return;
			Action.MEDIA_FAST_FORWARD.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniFF);
			break;
		case R.id.tricky_stop:
			if (!oneClickTapPress) return;
			Action.MEDIA_STOP.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniStop);
			break;
		case R.id.tricky_rew:
			if (!oneClickTapPress) return;
			Action.MEDIA_REWIND.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniRew);
			break;
		case R.id.tricky_pause:
			if (!oneClickTapPress) return;
			Action.MEDIA_PAUSE.execute(((BaseActivity)getActivity()).getCommands());
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniPause);
			break;
		}
	}

}
