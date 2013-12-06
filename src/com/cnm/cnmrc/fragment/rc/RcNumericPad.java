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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.BaseActivity;
import com.google.android.apps.tvremote.util.Action;

public class RcNumericPad extends Fragment implements View.OnClickListener {

	View layout;

	ImageButton m1, m2, m3, m4, m5, m6, m7, m8, m9, m0, mDel, mEnter; // numeric menu
	ImageButton m1_press, m2_press, m3_press, m4_press, m5_press; // press image
	ImageButton m6_press, m7_press, m8_press, m9_press, m0_press, mDel_press, mEnter_press; // press image

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		layout = inflater.inflate(R.layout.rc_numeric_pad, container, false);

		// remocon icon
		m1 = (ImageButton) layout.findViewById(R.id.numeric_n1);
		m1_press = (ImageButton) layout.findViewById(R.id.numeric_n1_press);
		m1.setOnTouchListener(touchListener);
		
		m2 = (ImageButton) layout.findViewById(R.id.numeric_n2);
		m2_press = (ImageButton) layout.findViewById(R.id.numeric_n2_press);
		m2.setOnTouchListener(touchListener);
		
		m3 = (ImageButton) layout.findViewById(R.id.numeric_n3);
		m3_press = (ImageButton) layout.findViewById(R.id.numeric_n3_press);
		m3.setOnTouchListener(touchListener);
		
		m4 = (ImageButton) layout.findViewById(R.id.numeric_n4);
		m4_press = (ImageButton) layout.findViewById(R.id.numeric_n4_press);
		m4.setOnTouchListener(touchListener);
		
		m5 = (ImageButton) layout.findViewById(R.id.numeric_n5);
		m5_press = (ImageButton) layout.findViewById(R.id.numeric_n5_press);
		m5.setOnTouchListener(touchListener);
		
		m6 = (ImageButton) layout.findViewById(R.id.numeric_n6);
		m6_press = (ImageButton) layout.findViewById(R.id.numeric_n6_press);
		m6.setOnTouchListener(touchListener);
		
		m7 = (ImageButton) layout.findViewById(R.id.numeric_n7);
		m7_press = (ImageButton) layout.findViewById(R.id.numeric_n7_press);
		m7.setOnTouchListener(touchListener);
		
		m8 = (ImageButton) layout.findViewById(R.id.numeric_n8);
		m8_press = (ImageButton) layout.findViewById(R.id.numeric_n8_press);
		m8.setOnTouchListener(touchListener);
		
		m9 = (ImageButton) layout.findViewById(R.id.numeric_n9);
		m9_press = (ImageButton) layout.findViewById(R.id.numeric_n9_press);
		m9.setOnTouchListener(touchListener);
		
		m0 = (ImageButton) layout.findViewById(R.id.numeric_n0);
		m0_press = (ImageButton) layout.findViewById(R.id.numeric_n0_press);
		m0.setOnTouchListener(touchListener);

		mDel = (ImageButton) layout.findViewById(R.id.numeric_del); 	// 지우기
		mDel_press = (ImageButton) layout.findViewById(R.id.numeric_del_press);
		mDel.setOnTouchListener(touchListener);
		
		mEnter = (ImageButton) layout.findViewById(R.id.numeric_enter); // 확인
		mEnter_press = (ImageButton) layout.findViewById(R.id.numeric_enter_press);
		mEnter.setOnTouchListener(touchListener);
		
		return layout;
	}

	View.OnTouchListener touchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				if (v.getTag().toString().equals("num_1")) {
					Action.NUM1.execute(((BaseActivity) getActivity()).getCommands());
					m1_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_2")) {
					Action.NUM2.execute(((BaseActivity) getActivity()).getCommands());
					m2_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_3")) {
					Action.NUM3.execute(((BaseActivity) getActivity()).getCommands());
					m3_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_4")) {
					Action.NUM4.execute(((BaseActivity) getActivity()).getCommands());
					m4_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_5")) {
					Action.NUM5.execute(((BaseActivity) getActivity()).getCommands());
					m5_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_6")) {
					Action.NUM6.execute(((BaseActivity) getActivity()).getCommands());
					m6_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_7")) {
					Action.NUM7.execute(((BaseActivity) getActivity()).getCommands());
					m7_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_8")) {
					Action.NUM8.execute(((BaseActivity) getActivity()).getCommands());
					m8_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_9")) {
					Action.NUM9.execute(((BaseActivity) getActivity()).getCommands());
					m9_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_0")) {
					Action.NUM0.execute(((BaseActivity) getActivity()).getCommands());
					m0_press.setVisibility(View.VISIBLE);
				}
				
				if (v.getTag().toString().equals("num_back")) {
					Action.DEL.execute(((BaseActivity) getActivity()).getCommands());
					mDel_press.setVisibility(View.VISIBLE);
				}
				if (v.getTag().toString().equals("num_ok")) {
					Action.ENTER.execute(((BaseActivity) getActivity()).getCommands());
					mEnter_press.setVisibility(View.VISIBLE);
				}

				return true;
			}
			
			
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (v.getTag().toString().equals("num_1")) {
					m1_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_2")) {
					m2_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_3")) {
					m3_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_4")) {
					m4_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_5")) {
					m5_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_6")) {
					m6_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_7")) {
					m7_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_8")) {
					m8_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_9")) {
					m9_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_0")) {
					m0_press.setVisibility(View.INVISIBLE);
					return true;
				}
				
				if (v.getTag().toString().equals("num_back")) {
					mDel_press.setVisibility(View.INVISIBLE);
					return true;
				}
				if (v.getTag().toString().equals("num_ok")) {
					mEnter_press.setVisibility(View.INVISIBLE);
					return true;
				}
			}
			return false;
		}
	};

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
		case R.id.numeric_n1:
			Action.NUM1.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n2:
			Action.NUM2.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n3:
			Action.NUM3.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n4:
			Action.NUM4.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n5:
			Action.NUM5.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n6:
			Action.NUM6.execute(((BaseActivity) getActivity()).getCommands());
			break;
		case R.id.numeric_n7:
			Action.NUM7.execute(((BaseActivity) getActivity()).getCommands());
			break;
		//		case R.id.numeric_n8:
		//			Action.NUM8.execute(((BaseActivity)getActivity()).getCommands());
		//			break;
		//		case R.id.numeric_n9:
		//			Action.NUM9.execute(((BaseActivity)getActivity()).getCommands());
		//			break;
		//		case R.id.numeric_n0:
		//			Action.NUM0.execute(((BaseActivity)getActivity()).getCommands());
		//			break;
		//			
		//		case R.id.numeric_del:
		//			Action.DEL.execute(((BaseActivity)getActivity()).getCommands());
		//			break;
		//		case R.id.numeric_enter:
		//			Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
		//			break;
		}

		/*
		 * // 테스트용... part 0 switch (v.getId()) { case R.id.numeric_n1: // DVR,
		 * 실시간TV와 같음
		 * Action.GO_TO_DVR.execute(((BaseActivity)getActivity()).getCommands
		 * ()); break; case R.id.numeric_n2: // guide, bookmark (tv프로그램 또는 영화)
		 * 전화면을 기억하여 북마크로 이동한다.
		 * Action.GO_TO_GUIDE.execute(((BaseActivity)getActivity
		 * ()).getCommands()); break; case R.id.numeric_n3: // Google TV 설정화면
		 * Action.SETTINGS.execute(((BaseActivity)getActivity()).getCommands());
		 * break; case R.id.numeric_n4: // 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
		 * Action.PAGE_UP.execute(((BaseActivity)getActivity()).getCommands());
		 * break; case R.id.numeric_n5: // 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
		 * Action.PAGE_DOWN
		 * .execute(((BaseActivity)getActivity()).getCommands()); break; case
		 * R.id.numeric_n6: // recall, ???
		 * Action.TAB.execute(((BaseActivity)getActivity()).getCommands());
		 * break; case R.id.numeric_n7: // 옵셔메뉴
		 * Action.GO_TO_MENU.execute(((BaseActivity
		 * )getActivity()).getCommands()); break;
		 * 
		 * case R.id.numeric_n8: // 검색
		 * Action.NAVBAR.execute(((BaseActivity)getActivity()).getCommands());
		 * //showActivity(KeyboardActivity.class); break; case R.id.numeric_n9:
		 * // 실시간 TV
		 * Action.GO_TO_LIVE_TV.execute(((BaseActivity)getActivity()).getCommands
		 * ()); break; case R.id.numeric_n0: // bookmark (tv프로그램 또는 영화) 전화면을
		 * 기억하여 북마크로 이동한다. tv모드일때만 book가 실행된다. home에서는 안된다. 그러나 guide는 home에서도
		 * 이동한다.
		 * Action.GO_TO_BOOKMARK.execute(((BaseActivity)getActivity()).getCommands
		 * ()); break;
		 * 
		 * case R.id.numeric_nback: Intent intent = getActivity().getIntent();
		 * intent.setAction(Intent.ACTION_SEND);
		 * intent.putExtra(Intent.EXTRA_TEXT, "http://daum.net");
		 * intent.setType("text/plain");
		 * ((MainActivity)getActivity()).flingIntent(intent); break; case
		 * R.id.numeric_nok:
		 * Action.ENTER.execute(((BaseActivity)getActivity()).getCommands());
		 * break; }
		 */

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
