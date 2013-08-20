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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.cnm.cnmrc.R;

public class RcFourWay extends RcBase implements View.OnClickListener{

	View layout;
	
	ImageButton up, down, left, right;
	ImageView aniUp, aniDown, aniLeft, aniRight;
	
	public static RcFourWay newInstance(String type) {
		RcFourWay f = new RcFourWay();
		Bundle args = new Bundle();
		args.putString("type", type);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_four_way, container, false);
		
		up = (ImageButton) layout.findViewById(R.id.fourway_up);
		aniUp = (ImageView) layout.findViewById(R.id.anim_fourway_up);
		up.setOnClickListener(this);
		
		right = (ImageButton) layout.findViewById(R.id.fourway_right);
		aniRight = (ImageView) layout.findViewById(R.id.anim_fourway_right);
		right.setOnClickListener(this);
		
		down = (ImageButton) layout.findViewById(R.id.fourway_down);
		aniDown = (ImageView) layout.findViewById(R.id.anim_fourway_down);
		down.setOnClickListener(this);
		
		left = (ImageButton) layout.findViewById(R.id.fourway_left);
		aniLeft = (ImageView) layout.findViewById(R.id.anim_fourway_left);
		left.setOnClickListener(this);

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
		
		switch(v.getId()){
		case R.id.fourway_up:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniUp);
			break;
		case R.id.fourway_right:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniRight);
			break;
		case R.id.fourway_down:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniDown);
			break;
		case R.id.fourway_left:
			if (!oneClickTapPress) return;
			oneClickTapPress = false;
			startLoadingAni((ImageButton) v, aniLeft);
			break;
		}
	}
	
}
