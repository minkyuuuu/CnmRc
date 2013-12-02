package com.cnm.cnmrc.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * This class overrides the onKeyPreIme method to dispatch a key event if the
 * KeyEvent passed to onKeyPreIme has a key code of KeyEvent.KEYCODE_BACK. This
 * allows key event listeners to detect that the soft keyboard was dismissed.
 */

public class ExtendedEditTextTextWatcherTest extends EditText {
	/**
	 * Will receive intercepted key events.
	 */
	public interface Interceptor {

		/**
		 * Called on non symbol key events.
		 * 
		 * @param event
		 *            : the key event
		 * @return {@code true} if the event was handled
		 */
		public boolean onKeyEvent(KeyEvent event);

		/**
		 * Called when a symbol is typed.
		 * 
		 * @param c
		 *            : the character being typed
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

	public ExtendedEditTextTextWatcherTest(Context context) {
		super(context);
		initialize();
	}

	public ExtendedEditTextTextWatcherTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ExtendedEditTextTextWatcherTest(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize();
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		// if not below, back key is not working!!!
		if (interceptor != null && interceptor.onKeyEvent(event)) {
			return true;
		}
		return super.dispatchKeyEventPreIme(event);
	}
	
	@Override
	// disable cursor position in EditText
    public void onSelectionChanged(int start, int end) {

        CharSequence text = getText();
        if (text != null) {
            if (start != text.length() || end != text.length()) {
                setSelection(text.length(), text.length());
                return;
            }
        }

        super.onSelectionChanged(start, end);
    }

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE; // android:imeOptions 보다 우선권이 높다.
		//outAttrs.privateImeOptions = "defaultInputmode=english";	// not working
		return new HwangInputConnection(super.onCreateInputConnection(outAttrs), true);

		// 아래의 BaseInputConnection 확장은 한글이 조합되어 보이지 않아 의미가 없다.
		// 따라서 setOnEditorActionListener은 따로 처리해 주어야한다.
		//return new InterceptConnection(this, true);		// Not show text in EditText
		//return new InterceptConnection(this, false);		// Show text in EditText, 그러나 한글 조합이 이상하다.
	}

	private class HwangInputConnection extends InputConnectionWrapper {

		public HwangInputConnection(InputConnection target, boolean mutable) {
			super(target, mutable);
		}

		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			// KeyEvent.KEYCODE_ENTER is not working on iterceptor
			if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
				interceptor.onKeyEvent(event);
				return true;
			}
			return super.sendKeyEvent(event);
		}
		
		@Override
		public boolean performEditorAction(int actionCode) {
			interceptor.onKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			return true;
		}
		
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
			}
			return super.setComposingText(text, newCursorPosition);
		}

		// if below 2 override method is not commented, Not show text in EditText
//		@Override
//		public boolean sendKeyEvent(KeyEvent event) {
//			interceptor.onKeyEvent(event);
//			return true;
//		}
//
//		@Override
//		public boolean commitText(CharSequence text, int newCursorPosition) {
//			for (int i = 0; i < text.length(); ++i) {
//				interceptor.onSymbol(text.charAt(i));
//			}
//			return true;
//		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (hasWindowFocus) {
			//requestFocus();
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

		int jong = (char) (s % 28);
		int jung = (char) ((((s - jong) / 28) % 21));
		int cho = (char) (((((s - jong) / 28) - jung) % 21));

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
