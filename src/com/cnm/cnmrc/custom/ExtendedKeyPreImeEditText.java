package com.cnm.cnmrc.custom;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * This class overrides the onKeyPreIme method to dispatch a key event if the
 * KeyEvent passed to onKeyPreIme has a key code of KeyEvent.KEYCODE_BACK.
 * This allows key event listeners to detect that the soft keyboard was
 * dismissed.
 *
 */

public class ExtendedKeyPreImeEditText extends EditText {
    Context mContext = null;
    Activity mActivity = null;

	public ExtendedKeyPreImeEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
	}

	public ExtendedKeyPreImeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) context;
	}

	public ExtendedKeyPreImeEditText(Context context) {
		super(context);
	}
	
    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        mActivity = (Activity) context;
    }

    // EditText의 경우 onKeyPreIme 메서드가 키보드가 내려가기 직전에 호출됩니다. EditText를 상속해서 다음 코드처럼 처리하면 됩니다.
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			//super.dispatchKeyEvent(event);
			//mActivity.finish();
//			FragmentManager fm = ((DeviceFinder)mActivity).getSupportFragmentManager();
//			PopupGtvManuallyInput popupGtvManuallyInput = (PopupGtvManuallyInput)((DeviceFinder)mActivity).getSupportFragmentManager().findFragmentByTag(PopupGtvManuallyInput.class.getSimpleName());
//			((DeviceFinder)mActivity).getSupportFragmentManager().beginTransaction().remove(popupGtvManuallyInput).commit();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent KEvent) 
	{
//	    int keyaction = KEvent.getAction();
//
//	    if(keyaction == KeyEvent.ACTION_DOWN)
//	    {
//	        int keycode = KEvent.getKeyCode();
//	        int keyunicode = KEvent.getUnicodeChar(KEvent.getMetaState() );
//	        char character = (char) keyunicode;
//
//	        System.out.println("DEBUG MESSAGE KEY=" + character + " KEYCODE=" +  keycode);
//	    }


	    return super.dispatchKeyEvent(KEvent);
	}
}
