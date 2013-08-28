package com.cnm.cnmrc.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Util {
	
	//네트워크 접속상태 확인 상수
	public final static int MOBILE_CONNECTED = 0;
	public final static int WIFI_CONNECTED = 1;
	public final static int WIMAX_CONNECTED = 6;
	public final static int DISCONNECTED = 99;

	public static void showSoftKeyboard(Context context, EditText editText) {
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
	}
	
	public static String getChannelAreaCode(Context context) {
		CnmPreferences pref = CnmPreferences.getInstance();
		return pref.loadChannelAreaCode(context);
	}
	public static void setChannelAreaCode(Context context, String value) {
		CnmPreferences pref = CnmPreferences.getInstance();
		pref.saveChannelAreaCode(context, value);
	}
	public static String getChannelAreaName(Context context) {
		CnmPreferences pref = CnmPreferences.getInstance();
		return pref.loadChannelAreaName(context);
	}
	public static void setChannelAreaName(Context context, String value) {
		CnmPreferences pref = CnmPreferences.getInstance();
		pref.saveChannelAreaName(context, value);
	}
	public static String getChannelProductName(Context context) {
		CnmPreferences pref = CnmPreferences.getInstance();
		return pref.loadChannelProductName(context);
	}
	public static void setChannelProductName(Context context, String value) {
		CnmPreferences pref = CnmPreferences.getInstance();
		pref.saveChannelProductName(context, value);
	}

	public static void AlertShow(Context context, String msg) {
		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
		alert_internet_status.setTitle("경 고");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("닫 기", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				//finish();
			}
		});
		AlertDialog alert = alert_internet_status.create();
		alert.show();
	}
	
	public static int GetNetworkInfo(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		if (ni != null) {
			switch (ni.getType()) {
			case ConnectivityManager.TYPE_MOBILE: {		// 3G (0)
				return MOBILE_CONNECTED;
			}
			case ConnectivityManager.TYPE_WIFI: {		// Wi-Fi (1)
				return WIFI_CONNECTED;
			}
			case ConnectivityManager.TYPE_WIMAX: {		// 4G (6)
				return WIMAX_CONNECTED;
			}
			}
		}
		return DISCONNECTED;
	}
}
