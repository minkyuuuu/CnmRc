package com.cnm.cnmrc.custom;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.cnm.cnmrc.CnmApplication;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.popup.PopupGtvConnection;

/**
 * @author hwangminkyu
 * 
 *         리모컨 앱의 버튼에 공통으로 효과음, 진동, 셋탑연결하기를 구현하는 base class
 */
public class ExtendedImageButton extends ImageButton {

	Context context;
	CnmApplication cnm;
	Activity activity;

	public ExtendedImageButton(Context context) {
		super(context);
		initialize(context);
	}

	public ExtendedImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	public ExtendedImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initialize(context);
	}

	private void initialize(Context context) {
		this.context = context;
		activity = (Activity) context;
		cnm = (CnmApplication) context.getApplicationContext();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			cnm.playSoundEffect();
			cnm.playVibratorEffect();
			
			boolean check = cnm.getCnmPrefernce().loadFirstConnectGtv(context);
			if(!check) {
				showPopupGtvConnection();
			}
			break;
		}
		
		return true;
		//return super.onTouchEvent(event);
	}
	
	public void showPopupGtvConnection() {
		FragmentTransaction ft = ((MainActivity)activity).getSupportFragmentManager().beginTransaction();
		PopupGtvConnection popupGtvConnection = new PopupGtvConnection();
		popupGtvConnection.show(ft, PopupGtvConnection.class.getSimpleName());
	}

}
