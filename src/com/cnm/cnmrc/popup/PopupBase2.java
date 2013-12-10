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

package com.cnm.cnmrc.popup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnm.cnmrc.R;

public class PopupBase2 extends DialogFragment  implements View.OnClickListener {
	
	TextView mTitle, mLine1, mLine2;
	Button mYes, mNo;
	ImageView mProgressBar;
	Animation animation;
	
	Context context;
	
	@Override
	public void onAttach(Activity activity) {
		context = activity;
		
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.popup2, container, false);
		
		animation = AnimationUtils.loadAnimation( context, R.anim.gtv_progressbar );
		mProgressBar = (ImageView) layout.findViewById(R.id.popup_progressbar);
		mProgressBar.setVisibility(View.INVISIBLE);
		
		mTitle = (TextView) layout.findViewById(R.id.popup_title);
		
		mLine1 = (TextView) layout.findViewById(R.id.popup_line_1);
		mLine2 = (TextView) layout.findViewById(R.id.popup_line_2);
		
		mYes = (Button) layout.findViewById(R.id.popup_yes);
		mYes.setText(getString(R.string.popup_yes));
		mYes.setOnClickListener(this);
		
		mNo = (Button) layout.findViewById(R.id.popup_no);
		mNo.setText(getString(R.string.popup_no));
		mNo.setOnClickListener(this);
		
		return layout;
	}

	@Override
	public void dismiss() {
		Log.d("hwang-tvremote", "PopupBase : dismiss()");
		super.dismiss();
	}

	@Override
	public void onDestroyView() {
		Log.d("hwang-tvremote", "PopupBase : onDestroyView()");
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		Log.d("hwang-tvremote", "PopupBase : onDetach()");
		super.onDetach();
	}

	@Override
	public void onStart() {
		Log.d("hwang-tvremote", "PopupBase : onStart()");
		super.onStart();

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		Log.d("hwang-tvremote", "PopupBase : onDismiss()");
		super.onDismiss(dialog);

	}

	@Override
	public void onClick(View v) {
		Log.d("hwang-tvremote", "PopupBase : onClick()");
		
	}



}
