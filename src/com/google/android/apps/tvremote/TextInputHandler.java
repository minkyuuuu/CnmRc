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

package com.google.android.apps.tvremote;

import android.content.Context;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.TextView;

import com.cnm.cnmrc.R;
import com.google.android.apps.tvremote.protocol.ICommandSender;
import com.google.android.apps.tvremote.util.Action;
import com.google.anymote.Key.Code;

/**
 * Handles text-related key input.
 * 
 * This class also manages a view that displays the last few characters typed.
 * 
 */
public final class TextInputHandler {

	/**
	 * Interface to send commands during a touch sequence.
	 */
	private final ICommandSender commands;

	/**
	 * Context.
	 */
	private final Context context;

	/**
	 * {@code true} if display should be cleared before next output.
	 */
	private boolean clearNextTime;
	
	private Boolean language = Boolean.valueOf(true);		// 폰의 기본이 영어라고 가정하자.<--true 	(셋탑이 한글이구 폰이 한글인경우...) <-- false    아니다. 큰 의미가 없다. 처음진입했을 때 처리해주어야한다.
	private Boolean languageBefore = Boolean.valueOf(true);	// 그 전 언어상태를 저장.
	private Boolean start = Boolean.valueOf(true);
	private Boolean startA = Boolean.valueOf(true);
	
	private Boolean statusStb = Boolean.valueOf(true); 		// (셋탑의 입력 언어 모드를 기본 영어모로 설정한다.statusStb =true) / (셋탑기준으로 한글일때는 최초 statusStb = false)

	public TextInputHandler(Context context, ICommandSender commands) {
		this.commands = commands;
		this.context = context;
	}
	
	/**
	 * The view that contains the visual feedback for text input.
	 */
	private TextView display;

	/**
	 * Handles a character input.
	 * 
	 * @param c
	 *            the character being typed
	 * @return {@code true} if the event was handled
	 */
	
	
	// ------------------------------------------------------------------------
	// 세탑의 언어상태를 먼저 생각하자.셋탑기준으로 영문과 한글모드를 기억하자.
	// 앱에서 빠져나갈 때 항상 셋탑의 상태를 정하자. 영문모드로.... 아래는 영문모드 기준이다.
	// 만약 한글기본모드로 셋탑상태로 앱을 빠져나가는 기준이라면 statusStb=false만 해주면 된다.
	//-------------------------------------------------------------------------
	// 영어처리, 숫자나 특수문자는 영어로 처리된다.
	public boolean handleChar(char c) {
		if (isValidCharacter(c)) {
			language = Boolean.TRUE;
			if(start) {
				languageBefore = Boolean.TRUE;
				start = Boolean.FALSE;
				
				// (Test SWITCH_CHARSET)
				if(!statusStb) {	// 앱의 입력 언어 모드가 영문일때. (세탑기준으로 영문일때 최초 statusStb = true) (셋탑기준으로 한글일때는 최초 statusStb = false만 바꾸어주면 된다.)
					Action.SWITCH_CHARSET.execute(commands);		// 세탑이 기본 한글 입력모드이고, 폰에서 영어 진입시 영어변환해주어야한다. 최초에만 맞추어주면된다.
					statusStb = Boolean.TRUE;
				}
				
				// Real (SWITCH_CHARSET_ENG, SWITCH_CHARSET_KOR)
				//Action.SWITCH_CHARSET_ENG.execute(commands);	// 이 경우는 무조건 현재 폰의 상태를 세탑에 보내준다.
			}
			else {
				if(languageBefore.compareTo(language) != 0) {
					languageBefore = Boolean.TRUE;
					Action.SWITCH_CHARSET.execute(commands);
					statusStb = Boolean.TRUE;
				} 
			}
			
			String str = String.valueOf(c);
			appendDisplayedText(str);
			commands.string(str);
			return true;
		}
		return false;
	}
	
	// 한글처리
	public boolean handleCharA(char c) {
		if (isValidCharacter(c)) {
			language = Boolean.FALSE;
			if(startA) {
				languageBefore = Boolean.FALSE;
				startA = Boolean.FALSE;
				
				// (Test SWITCH_CHARSET)
				if(statusStb) {	// 앱의 입력 언어 모드가 한글일때. (세탑기준으로 영문일때 최초 statusStb = true) (셋탑기준으로 한글일때는 최초 statusStb = false만 바꾸어주면 된다.)
					Action.SWITCH_CHARSET.execute(commands);		// 세탑이 기본 영어 입력모드이고, 폰에서 한글 진입시 한글변환해주어야한다. 최초에만 맞추어주면된다.
					statusStb = Boolean.FALSE;
				}
				
				// Real (SWITCH_CHARSET_ENG, SWITCH_CHARSET_KOR)
				//Action.SWITCH_CHARSET_KOR.execute(commands);	// 이 경우는 무조건 현재 폰의 상태를 세탑에 보내준다.
			}
			else {
				if(languageBefore.compareTo(language) != 0) {
					languageBefore = Boolean.FALSE;
					Action.SWITCH_CHARSET.execute(commands);
					statusStb = Boolean.FALSE;
				} 
			}
			
			String str = String.valueOf(c);
			appendDisplayedText(str);
			
			// 한글 문자 보내기
			sendHangul(str);	// 한글을 해당하는 영문자판의 문자로보낸다.
			//sendHangul2();	// 한글을 해당하는 키코드로 보낸다.
			//commands.string(str);
			
			return true;
		}
		return false;
	}

	private void sendHangul(String str) {
		//if(str.equals("ㅂ")) commands.string("q");
		if(str.equals("ㅂ")) {
			Action.CHAR_Q.execute(commands);	// same above~~
			return;
		}
		
		if(str.equals("ㅈ")) {
			commands.string("w");
			return;
		}
		if(str.equals("ㄷ")) {
			commands.string("e");
			return;
		}
		if(str.equals("ㄱ")) {
			commands.string("r");
			return;
		}
		if(str.equals("ㅅ")) {
			commands.string("t");
			return;
		}
		if(str.equals("ㅛ")) {
			commands.string("y");
			return;
		}
		if(str.equals("ㅕ")) {
			commands.string("u");
			return;
		}
		if(str.equals("ㅑ")) {
			commands.string("i");
			return;
		}
		if(str.equals("ㅐ")) {
			commands.string("o");
			return;
		}
		if(str.equals("ㅔ")) {
			commands.string("p");
			return;
		}
		
		if(str.equals("ㅁ")) {
			commands.string("a");
			return;
		}
		if(str.equals("ㄴ")) {
			commands.string("s");
			return;
		}
		if(str.equals("ㅇ")) {
			commands.string("d");
			return;
		}
		if(str.equals("ㄹ")) {
			commands.string("f");
			return;
		}
		if(str.equals("ㅎ")) {
			commands.string("g");
			return;
		}
		if(str.equals("ㅗ")) {
			commands.string("h");
			return;
		}
		if(str.equals("ㅓ")) {
			commands.string("j");
			return;
		}
		if(str.equals("ㅏ")) {
			commands.string("k");
			return;
		}
		if(str.equals("ㅣ")) {
			commands.string("l");
			return;
		}
		
		if(str.equals("ㅋ")) {
			commands.string("z");
			return;
		}
		if(str.equals("ㅌ")) {
			commands.string("x");
			return;
		}
		if(str.equals("ㅊ")) {
			commands.string("c");
			return;
		}
		if(str.equals("ㅍ")) {
			commands.string("v");
			return;
		}
		if(str.equals("ㅠ")) {
			commands.string("b");
			return;
		}
		if(str.equals("ㅜ")) {
			commands.string("n");
			return;
		}
		if(str.equals("ㅡ")) {
			commands.string("m");
			return;
		}
		
		// shift
		// 영문과 한글의 shift에 대한 우선순위가 있는것 같다.
		if(str.equals("ㅃ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("Q");
			return;
			
		}
		if(str.equals("ㅉ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("W");
			return;
		}
		if(str.equals("ㄸ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("E");
			return;
		}
		if(str.equals("ㄲ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("R");
			return;
		}
		if(str.equals("ㅆ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("T");
			return;
		}
		if(str.equals("ㅒ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("O"); return;
		}
		if(str.equals("ㅖ")) {
			//Action.SHIFT_LEFT.execute(commands);
			commands.string("P");
			return;
		}
		
	}

	/**
	 * Handles a key event.
	 * 
	 * @param event
	 *            the key event to handle
	 * @return {@code true} if the event was handled
	 */
	public boolean handleKey(KeyEvent event) {
		if (event.getAction() != KeyEvent.ACTION_DOWN) {
			return false;
		}
		int code = event.getKeyCode();
//		if (code == KeyEvent.KEYCODE_ENTER) {
//			displaySingleTimeMessage(context.getString(R.string.keyboard_enter));
//			Action.ENTER.execute(commands);
//			return true;
//		}
		if (code == KeyEvent.KEYCODE_DEL) {
			displaySingleTimeMessage(context.getString(R.string.keyboard_del));
			Action.DEL.execute(commands);
			return true;
		}

		if (code == KeyEvent.KEYCODE_SPACE) {
			appendDisplayedText(" ");
			commands.keyPress(Code.KEYCODE_SPACE);
			return true;
		}

		int c = event.getUnicodeChar();
		return handleChar((char) c);
	}

	private boolean isValidCharacter(int unicode) {
		// 한글자모 		: 10a0 ~ 318f (4256 ~ 4351)
		// 호환용 한글자모 	: 3130 ~ 318f (12592 ~ 12687)
		// 한글자모확장-A 	: a960 ~ a97f (43360 ~ 43391) 32ea
		// 한글음절 		: ac00 ~ d7af (44032 ~ 55215) 11184ea
		// 한글자모확장-B 	: d7b0 ~ d7ff (55216 ~ 55295) 80ea
//		return (unicode > 0 && unicode < 256) || (unicode > 4255 && unicode < 4352);	// 한글자모
//		return (unicode > 0 && unicode < 256) || (unicode > 12591 && unicode < 12688);	// 호환용 한글자모
		
//		return (unicode > 0 && unicode < 256) || (unicode > 43359 && unicode < 43392) || (unicode > 55215 && unicode < 55296);	// 조합형?
//		return (unicode > 0 && unicode < 256) || (unicode > 43359 && unicode < 43392);	// 조합형 A
//		return (unicode > 0 && unicode < 256) || (unicode > 55215 && unicode < 55296);	// 조합형 B
//		return (unicode > 0 && unicode < 256) || (unicode > 44032 && unicode < 55215);	// 완성형 
//		return (unicode > 0 && unicode < 256) || (unicode > 43359 && unicode < 55296);  // 조합형 & 완성형S
		//return (unicode > 0 && unicode < 256);
		return (unicode > 0 && unicode < 256) || (unicode > 12591 && unicode < 12688);	// 호환용 한글자모
	}

	public void setDisplay(TextView textView) {
		display = textView;
	}

	private void appendDisplayedText(CharSequence seq) {
		if (display != null) {
			if (!clearNextTime) {
				seq = new StringBuffer(display.getText()).append(seq);
			}
			display.setText(seq);
		}
		clearNextTime = false;
	}

	private void displaySingleTimeMessage(CharSequence seq) {
		if (display != null) {
			display.setText(seq);
			clearNextTime = true;
		}
	}
}
