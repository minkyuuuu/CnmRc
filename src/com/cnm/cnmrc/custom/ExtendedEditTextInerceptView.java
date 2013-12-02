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

public class ExtendedEditTextInerceptView extends EditText {
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

	public ExtendedEditTextInerceptView(Context context) {
		super(context);
		initialize();
	}

	public ExtendedEditTextInerceptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ExtendedEditTextInerceptView(Context context, AttributeSet attrs, int defStyle) {
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
		outAttrs.privateImeOptions = "defaultInputmode=english";	// not working

		// 아래의 BaseInputConnection 확장은 한글이 조합되어 보이지 않아 의미가 없다.
		// 따라서 setOnEditorActionListener은 따로 처리해 주어야한다.
		return new InterceptConnection(this, true);		// Not show text in EditText
		//return new InterceptConnection(this, false);		// Show text in EditText, 그러나 한글 조합이 이상하다.
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


}
