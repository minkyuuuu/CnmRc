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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.util.Action;

public class RcNumericPad extends Fragment implements View.OnClickListener {

	View layout;

	ImageButton m1, m2, m3, m4, m5, m6, m7, m8, m9, m0, mBack, mOk; // numeric
																	// menu

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_numeric_pad, container, false);

		// remocon icon
		m1 = (ImageButton) layout.findViewById(R.id.numeric_n1);
		m2 = (ImageButton) layout.findViewById(R.id.numeric_n2);
		m3 = (ImageButton) layout.findViewById(R.id.numeric_n3);
		m4 = (ImageButton) layout.findViewById(R.id.numeric_n4);
		m5 = (ImageButton) layout.findViewById(R.id.numeric_n5);
		m6 = (ImageButton) layout.findViewById(R.id.numeric_n6);
		m7 = (ImageButton) layout.findViewById(R.id.numeric_n7);
		m8 = (ImageButton) layout.findViewById(R.id.numeric_n8);
		m9 = (ImageButton) layout.findViewById(R.id.numeric_n9);
		m0 = (ImageButton) layout.findViewById(R.id.numeric_n0);

		mBack = (ImageButton) layout.findViewById(R.id.numeric_nback); // 지우기
		mOk = (ImageButton) layout.findViewById(R.id.numeric_nok); // 확인

		m1.setOnClickListener(this);
		m2.setOnClickListener(this);
		m3.setOnClickListener(this);
		m4.setOnClickListener(this);
		m5.setOnClickListener(this);
		m6.setOnClickListener(this);
		m7.setOnClickListener(this);
		m8.setOnClickListener(this);
		m9.setOnClickListener(this);
		m0.setOnClickListener(this);

		mBack.setOnClickListener(this);
		mOk.setOnClickListener(this);

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

/*		switch (v.getId()) {
		case R.id.numeric_n1:
			Action.NUM1.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n2:
			Action.NUM2.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n3:
			Action.NUM3.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n4:
			Action.NUM4.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n5:
			Action.NUM5.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n6:
			Action.NUM6.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n7:
			Action.NUM7.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n8:
			Action.NUM8.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n9:
			Action.NUM9.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n0:
			Action.NUM0.execute(((BaseActivity)getActivity()).getCommands());
			break;
			
		case R.id.numeric_nback:
			Action.BACKSPACE.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_nok:
			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
			break;
		}*/
		
		
		
		// 테스트용... part 0
		switch (v.getId()) {
		case R.id.numeric_n1:	// DVR, 실시간TV와 같음
			Action.GO_TO_DVR.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n2:	// guide, bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. 
			Action.GO_TO_GUIDE.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n3:	// Google TV 설정화면
			Action.SETTINGS.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n4:	// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
			Action.PAGE_UP.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n5:	// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
			Action.PAGE_DOWN.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n6:	// recall, ???
			Action.TAB.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n7:	// 옵셔메뉴
			Action.GO_TO_MENU.execute(((BaseActivity)getActivity()).getCommands());
			break;
			
		case R.id.numeric_n8:	// 검색
	        Action.NAVBAR.execute(((BaseActivity)getActivity()).getCommands());
	        //showActivity(KeyboardActivity.class);
			break;
		case R.id.numeric_n9:	// 실시간 TV
			Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands());
			break;
		case R.id.numeric_n0:	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도 이동한다.
			Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands());
			break;
			
		case R.id.numeric_nback:
		    Intent intent = getActivity().getIntent();
		    intent.setAction(Intent.ACTION_SEND);
		    intent.putExtra(Intent.EXTRA_TEXT, "http://daum.net");
		    intent.setType("text/plain");
		    ((MainActivity)getActivity()).flingIntent(intent);
			break;
		case R.id.numeric_nok:
			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
			break;
		}
		
		
		// 테스트용... part 1
//		switch (v.getId()) {
//		case R.id.numeric_n1:	// DVR, 실시간TV와 같음
//			Action.GO_TO_DVR.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n2:	// guide, bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. 
//			Action.GO_TO_GUIDE.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n3:	// info, 아무런 동작을 하지 않는다. ???
//			Action.GO_TO_INFO.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n4:	// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
//			Action.PAGE_UP.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n5:	// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
//			Action.PAGE_DOWN.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n6:	// recall, ???
//			Action.TAB.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n7:	// 옵셔메뉴
//			Action.GO_TO_MENU.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_n8:	// 검색
//	        Action.NAVBAR.execute(((BaseActivity)getActivity()).getCommands());
//	        //showActivity(KeyboardActivity.class);
//			break;
//		case R.id.numeric_n9:	// 실시간 TV
//			Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n0:	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도 이동한다.
//			Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_nback:
//			Action.BACKSPACE.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_nok:
//			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		}
		
		// 테스트용... part 2
//		switch (v.getId()) {
//		case R.id.numeric_n1:	// Google TV 설정화면
//			Action.SETTINGS.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n2:	// x
//			Action.SETUP.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n3:	// x
//			Action.EXPLORER.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n4:	// char '*'
//			Action.STAR.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n5:	// x
//			Action.ZOOM_IN.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n6:	// x
//			Action.ZOOM_OUT.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n7:	// 옵셔메뉴
//			Action.GO_TO_MENU.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_n8:	// 검색
//			Action.NAVBAR.execute(((BaseActivity)getActivity()).getCommands());
//			//showActivity(KeyboardActivity.class);
//			break;
//		case R.id.numeric_n9:	// 실시간 TV
//			Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n0:	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도 이동한다.
//			Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_nback:
//			Action.BACKSPACE.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_nok:
//			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		}
		
		// 테스트용... part 3
//		switch (v.getId()) {
//		case R.id.numeric_n1:	// ckick
//			Action.CLICK_DOWN.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n2:	// click
//			Action.CLICK_UP.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n3:	// x
//			Action.BD_TOP_MENU.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n4:	// x
//			Action.BD_MENU.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n5:	// x
//			Action.EJECT.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n6:	// x
//			Action.AUDIO.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n7:	// x
//			Action.CAPTIONS.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_n8:	// 검색
//			Action.NAVBAR.execute(((BaseActivity)getActivity()).getCommands());
//			//showActivity(KeyboardActivity.class);
//			break;
//		case R.id.numeric_n9:	// 실시간 TV
//			Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n0:	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도 이동한다.
//			Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_nback:
//			Action.BACKSPACE.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_nok:
//			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		}
		
		// 테스트용... part 4
//		switch (v.getId()) {
//		case R.id.numeric_n1:	// x
//			Action.POWER_STB.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n2:	// x
//			Action.INPUT_STB.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n3:	// x
//			Action.POWER_BD.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n4:	// x
//			Action.INPUT_BD.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n5:	// x
//			Action.POWER_AVR.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n6:	// x
//			Action.INPUT_AVR.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n7:	// x
//			Action.POWER_TV.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_n8:	// x
//			Action.INPUT_TV.execute(((BaseActivity)getActivity()).getCommands());
//			//showActivity(KeyboardActivity.class);
//			break;
//		case R.id.numeric_n9:	// 실시간 TV
//			Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_n0:	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도 이동한다.
//			Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//			
//		case R.id.numeric_nback:
//			Action.BACKSPACE.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		case R.id.numeric_nok:
//			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
//			break;
//		}
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
