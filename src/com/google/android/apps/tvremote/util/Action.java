/*
 * Copyright (C) 2009 Google Inc.  All rights reserved.
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

package com.google.android.apps.tvremote.util;

import com.google.android.apps.tvremote.protocol.ICommandSender;
import com.google.anymote.Key;
import com.google.anymote.Key.Code;

/**
 * Lists common control actions on a Google TV box.
 * 
 */
public enum Action {

	// -----------------------------
	// MainActivity > top menu
	// -----------------------------
	// KEYCODE_POWER(o), KEYCODE_BD_POWER(x), KEYCODE_STB_POWER(x), KEYCODE_TV_POWER(x)  KEYCODE_AVR_POWER(x) 
	POWER {		// 기획의도는 셋탑전원인데 ...
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_POWER);
		}
	},
	HOME {		// 통합 ui
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_HOME);
		}
	},
	//	MOVE_HOME {
	//		@Override
	//		public void execute(ICommandSender sender) {
	//			sender.keyPress(Code.KEYCODE_MOVE_HOME);
	//		}
	//	},
	
	
	// -------------------------------
	// MainActivity > channel&volume
	// -------------------------------
	VOLUME_UP {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_VOLUME_UP);
		}
	},
	MUTE {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MUTE);
		}
	},
	VOLUME_DOWN {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_VOLUME_DOWN);
		}
	},

	CHANNEL_UP {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_CHANNEL_UP);
		}
	},
	CHANNEL_DOWN {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_CHANNEL_DOWN);
		}
	},
	
	
	
	
	// -------------------------------
	// MainActivity > 사방향
	// -------------------------------
	DPAD_CENTER {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DPAD_CENTER);
		}
	},
	DPAD_UP {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DPAD_UP);
		}
	},
	DPAD_UP_PRESSED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_UP, Key.Action.DOWN);
		}
	},
	DPAD_UP_RELEASED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_UP, Key.Action.UP);
		}
	},
	DPAD_RIGHT {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DPAD_RIGHT);
		}
	},
	DPAD_RIGHT_PRESSED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_RIGHT, Key.Action.DOWN);
		}
	},
	DPAD_RIGHT_RELEASED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_RIGHT, Key.Action.UP);
		}
	},
	DPAD_DOWN {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DPAD_DOWN);
		}
	},
	DPAD_DOWN_PRESSED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_DOWN, Key.Action.DOWN);
		}
	},
	DPAD_DOWN_RELEASED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_DOWN, Key.Action.UP);
		}
	},
	DPAD_LEFT {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DPAD_LEFT);
		}
	},
	DPAD_LEFT_PRESSED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_LEFT, Key.Action.DOWN);
		}
	},
	DPAD_LEFT_RELEASED {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.KEYCODE_DPAD_LEFT, Key.Action.UP);
		}
	},


	
	
	
	// -----------------------------------------------------------------------------
	// MainActivity > Trick Play Button
	// VCR-like functions such as pause, rewind, fast forward, replay and skip, 
	// collectively known as 'trick play', all while rendering a live video stream.
	// -----------------------------------------------------------------------------
	MEDIA_PLAY {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MEDIA_PLAY);
		}
	},
	MEDIA_FAST_FORWARD {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MEDIA_FAST_FORWARD);
		}
	},
	MEDIA_STOP {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MEDIA_STOP);
		}
	},
	MEDIA_REWIND {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MEDIA_REWIND);
		}
	},
	MEDIA_PAUSE {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PAUSE);
		}
	},
	
	
	
	
	// --------------------------------
	// MainActivity > 숫자키, 지우기, 확인
	// --------------------------------
	NUM0 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_0);
		}
	},
	NUM1 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_1);
		}
	},
	NUM2 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_2);
		}
	},
	NUM3 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_3);
		}
	},
	NUM4 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_4);
		}
	},
	NUM5 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_5);
		}
	},
	NUM6 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_6);
		}
	},
	NUM7 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_7);
		}
	},
	NUM8 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_8);
		}
	},
	NUM9 {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_9);
		}
	},
	BACKSPACE {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DEL);
		}
	},
	ENTER {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_ENTER);
		}
	},
	
	

	// -----------------------------------------------
	// MainActivity > 뒤로가기, 나가기 : 둘다 취소의 의미이다.
	// -----------------------------------------------
	BACK {			// 뒤로가기
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BACK);
		}
	},
	ESCAPE {		// 나가기
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_ESCAPE);
		}
	},
	
	// --------------------------------
	// MainActivity > 프로그램키?
	// --------------------------------
	COLOR_RED {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PROG_RED);
		}
	},
	COLOR_GREEN {		// 선호채널
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PROG_GREEN);
		}
	},
	COLOR_YELLOW {		// 보기전환
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PROG_YELLOW);
		}
	},
	COLOR_BLUE {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PROG_BLUE);
		}
	},
	
	
	

	
	// ----------------------------------------
	// 현 프로그램에서는 적용하지 않는 키들 (테스트해보자)
	// ----------------------------------------
	NAVBAR {			// 검색
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_SEARCH);
		}
	},
	GO_TO_LIVE_TV {		// 실시간 TV
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_LIVE);
		}
	},
	GO_TO_BOOKMARK {	// bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다.
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BOOKMARK);
		}
	},
	
	
	
	GO_TO_DVR {			// DVR, 실시간TV와 같음
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_DVR);
		}
	},
	GO_TO_GUIDE {		// guide, bookmark (tv프로그램 또는 영화) 전화면을 기억하여 북마크로 이동한다. 
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_GUIDE);
		}
	},
	GO_TO_INFO {		// info, 아무런 동작을 하지 않는다. ???
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_INFO);
		}
	},
	PAGE_UP {			// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PAGE_UP);
		}
	},
	PAGE_DOWN {			// 브라우저에서 페이지단위 이동, 볼륨키도 적용된다.
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_PAGE_DOWN);
		}
	},
	TAB {				// recall, ???
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_TAB);
		}
	},
	GO_TO_MENU {		// 옵셔메뉴
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_MENU);
		}
	},
	
	
	// ---------------
	// test 2 part
	// ---------------
	SETTINGS {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_SETTINGS);
		}
	},
	SETUP {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_SETUP);
		}
	},
	EXPLORER {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_EXPLORER);
		}
	},
	STAR {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_STAR);
		}
	},
	ZOOM_IN {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_ZOOM_IN);
		}
	},
	ZOOM_OUT {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_ZOOM_OUT);
		}
	},

	

	
//	FORWARD_DEL {
//		@Override
//		public void execute(ICommandSender sender) {
//			sender.keyPress(Code.KEYCODE_FORWARD_DEL);
//		}
//	},
//	MEDIA_CLOSE {
//		@Override
//		public void execute(ICommandSender sender) {
//			sender.keyPress(Code.KEYCODE_MEDIA_CLOSE);
//		}
//	},
	
	
	
	
	
	
	// ---------------
	// test 3 part
	// ---------------
	CLICK_DOWN {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.BTN_MOUSE, Key.Action.DOWN);
		}
	},

	CLICK_UP {
		@Override
		public void execute(ICommandSender sender) {
			sender.key(Code.BTN_MOUSE, Key.Action.UP);
		}
	},



	BD_TOP_MENU {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BD_TOP_MENU);
		}
	},
	BD_MENU {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BD_POPUP_MENU);
		}
	},
	EJECT {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_EJECT);
		}
	},
	AUDIO {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_AUDIO);
		}
	},
	CAPTIONS {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_INSERT);
		}

	},



	// ---------------
	// test 4 part
	// ---------------
	POWER_STB {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_STB_POWER);
		}
	},
	INPUT_STB {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_STB_INPUT);
		}
	},
	POWER_BD {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BD_POWER);
		}
	},
	INPUT_BD {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_BD_INPUT);
		}
	},
	POWER_AVR {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_AVR_POWER);
		}
	},
	INPUT_AVR {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_AVR_INPUT);
		}
	},
	POWER_TV {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_TV_POWER);
		}
	},
	INPUT_TV {
		@Override
		public void execute(ICommandSender sender) {
			sender.keyPress(Code.KEYCODE_TV_INPUT);
		}
	};

	
	
	
	

	/**
	 * Executes the action.
	 * 
	 * @param sender
	 *            interface to the remote box
	 */
	public abstract void execute(ICommandSender sender);
}
