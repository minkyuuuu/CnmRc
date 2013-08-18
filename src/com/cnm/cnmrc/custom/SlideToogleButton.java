package com.cnm.cnmrc.custom;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.QwertyFragment.CallbackQwerty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@SuppressWarnings("deprecation")
public class SlideToogleButton extends SlidingDrawer implements Checkable, OnDrawerOpenListener, OnDrawerCloseListener {
	int mButtonResource = 0;
	OnCheckChangedListner mOnCheckChangedListner;
	
//	public interface CallbacksConfig {
//		public void onEventAutoAdultOn();
//		public void onEventAutoAdultOff();
//	}
//	
//	private CallbacksConfig mCallbacksConfig = sDummyCallback;

	public SlideToogleButton(Context context, AttributeSet attr) {
		super(context, attr);

		setOnDrawerOpenListener(this);
		setOnDrawerCloseListener(this);
	}

	@Override
	public boolean isChecked() {
		// TODO Auto-generated method stub
		return !isOpened();
	}

	@Override
	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		if (!isOpened() != checked) {
			if (checked) {
				this.animateClose();
			} else {
				this.animateOpen();
			}
		}

	}

	@Override
	public void toggle() {
		// TODO Auto-generated method stub
		if (!isOpened()) {
			this.animateOpen();
		} else {
			this.animateClose();
		}

	}

	public interface OnCheckChangedListner {
		public void onCheckChanged(View v, boolean isChecked);
	}

	@Override
	public void onDrawerOpened() {
		// TODO Auto-generated method stub
		if (mOnCheckChangedListner != null) {
			mOnCheckChangedListner.onCheckChanged(this, isChecked());
			this.getHandle().setBackgroundResource(R.drawable.switch_off);
		}
	}

	@Override
	public void onDrawerClosed() {
		// TODO Auto-generated method stub
		if (mOnCheckChangedListner != null) {
			mOnCheckChangedListner.onCheckChanged(this, isChecked());
			this.getHandle().setBackgroundResource(R.drawable.switch_on);
		}
	}

	public OnCheckChangedListner getOnCheckChangedListner() {
		return mOnCheckChangedListner;
	}

	public void setOnCheckChangedListner(OnCheckChangedListner onCheckChangedListner) {
		this.mOnCheckChangedListner = onCheckChangedListner;
	}
}