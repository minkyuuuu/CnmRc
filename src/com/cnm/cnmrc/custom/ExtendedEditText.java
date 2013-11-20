package com.cnm.cnmrc.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * This class overrides the onKeyPreIme method to dispatch a key event if the
 * KeyEvent passed to onKeyPreIme has a key code of KeyEvent.KEYCODE_BACK. This
 * allows key event listeners to detect that the soft keyboard was dismissed.
 * 
 */

public class ExtendedEditText extends EditText {
	/**
	 * Will receive intercepted key events.
	 */
	public interface Interceptor {

		/**
		 * Called on non symbol key events.
		 * 
		 * @param event
		 *            the key event
		 * @return {@code true} if the event was handled
		 */
		public boolean onKeyEvent(KeyEvent event);

		/**
		 * Called when a symbol is typed.
		 * 
		 * @param c
		 *            the character being typed
		 * @return {@code true} if the event was handled
		 */
		public boolean onSymbol(char c);
	}

	private Interceptor interceptor;

	private void initialize() {
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	Context mContext = null;
	Activity mActivity = null;

	public ExtendedEditText(Context context) {
		super(context);
		initialize();
	}

	public ExtendedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		if (interceptor != null && interceptor.onKeyEvent(event)) {
			return true;
		}
		return super.dispatchKeyEventPreIme(event);
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		//outAttrs.privateImeOptions = "defaultInputmode=english";
		outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
		return new InterceptConnection(this, true);
	}

	/**
	 * A class that intercepts events from the soft keyboard.
	 */
	private final class InterceptConnection extends BaseInputConnection {
		public InterceptConnection(View targetView, boolean fullEditor) {
			super(targetView, fullEditor);
		}

		@Override
		public boolean performEditorAction(int actionCode) {
			interceptor.onKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			return true;
		}

		@Override
		public boolean setComposingText(CharSequence text, int newCursorPosition) {
			for (int i = 0; i < text.length(); ++i) {
				interceptor.onSymbol(text.charAt(i));
//				int[] a = divideHangul(text.charAt(i));
//				interceptor.onSymbol((char)a[0]);
			}
			return super.setComposingText(text, newCursorPosition);
			// return super.finishComposingText(); // x
			//super.deleteSurroundingText(1, 0);
			//return super.commitText("", newCursorPosition);
			//return true;
		}

		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			interceptor.onKeyEvent(event);
			return true;
		}

		@Override
		public boolean commitText(CharSequence text, int newCursorPosition) {
			for (int i = 0; i < text.length(); ++i) {
				interceptor.onSymbol(text.charAt(i));
			}
			return true;
		}

//		@Override
//		public CharSequence getTextBeforeCursor(int length, int flags) {
//			// TODO Auto-generated method stub
//			CharSequence text = super.getTextBeforeCursor(length, flags);
//			return text;
//		}

//		@Override
//		public CharSequence getSelectedText(int flags) {
//			// TODO Auto-generated method stub
//			return super.getSelectedText(flags);
//		}
//
//		@Override
//		public CharSequence getTextAfterCursor(int length, int flags) {
//			// TODO Auto-generated method stub
//			return super.getTextAfterCursor(length, flags);
//		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			requestFocus();
			showKeyboard();
		} else {
			hideKeyboard();
		}
	}

	private void hideKeyboard() {
		getInputManager().hideSoftInputFromWindow(getWindowToken(), 0 /* no flag */);
	}

	private void showKeyboard() {
		InputMethodManager manager = getInputManager();
		manager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * Gets access to the system input manager.
	 */
	private InputMethodManager getInputManager() {
		return (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	
	public static int[] divideHangul(char s) {  
	    int[] result = new int[3];  
	    
	    int jong = (char)(s % 28);
	    int jung = (char)( (((s-jong)/28) % 21) );
	    int cho = (char)( ((((s-jong)/28) - jung) % 21) );
	    		
	    result[0] = 0x1100 + cho;  
	    result[1] = 0x1161 + jung;  
	    result[2] = 0x11a7 + jong;  
	    return result;  
	    
//		char uniVal = (char)comVal;
//		// 유니코드 표에 맞추어 초성 중성 종성을 분리합니다..
//		char cho = (char) ((((uniVal - (uniVal % 28)) / 28) / 21) + 0x1100);
//		char jung = (char) ((((uniVal - (uniVal % 28)) / 28) % 21) + 0x1161);
//		char jong = (char) ((uniVal % 28) + 0x11a7);
	    
//	    초성 = ( ( (characterValue - 종성 ) / 28 ) - 중성 ) / 21
//	    중성 = ( (characterValue - 종성 ) / 28 ) % 21
//	    종성 = characterValue % 28
//
//	    이 값들은 인덱스 값이지 실제 Unicode 테이블표에 매치되지 않는다. 
//	    매치 시키려면 각각 초성 중성 종성의 시작 unicode 값들을 더해주면 된다.
//	    ( 초성 : 0x1100   , 중성 : 0x1161   , 종성 : 0x11A7 )
	}  
	
	//	private void init(Context context, AttributeSet attrs) {
	//		mContext = context;
	//		mActivity = (Activity) context;
	//	}

	// EditText의 경우 onKeyPreIme 메서드가 키보드가 내려가기 직전에 호출됩니다. EditText를 상속해서 다음 코드처럼 처리하면 됩니다.
	//	@Override
	//	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
	//		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
	//			//super.dispatchKeyEvent(event);
	//			mActivity.finish();
	//			return true;
	//		}
	//		return false;
	//	}

}
