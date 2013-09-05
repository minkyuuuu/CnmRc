package com.cnm.cnmrc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.cnm.cnmrc.R;

public class Util {
	
	//네트워크 접속상태 확인 상수
	public final static int MOBILE_CONNECTED = 0;
	public final static int WIFI_CONNECTED = 1;
	public final static int WIMAX_CONNECTED = 6;
	public final static int DISCONNECTED = 99;		// network is not available!!!

	
	// --------------
	// Date
	// --------------
	public static Date toDate(int yyyy,int mm,int dd){
		Calendar cal  = Calendar.getInstance();
		cal.set(Calendar.YEAR, yyyy);
		cal.set(Calendar.MONTH, mm);
		cal.set(Calendar.DAY_OF_MONTH, dd);
		return cal.getTime();
	}
	public static Calendar toCalendar(Date solraFixedDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(solraFixedDate);
		return calendar;
	}
	public static String getYYYYMMDD(Date date){
		if(date==null) return "";
		return new SimpleDateFormat("yyyyMMdd").format(date);
	}
	public static String getYYYYMMDDwithPeriod(Date date){
		if(date==null) return "";
		return new SimpleDateFormat("yyyy.MM.dd",Locale.getDefault()).format(date);
	}
	
	// ------------------------
	// error code description
	// ------------------------
	public static String getErrorCodeDesc(String code){
		for(ErrorCode errorCode : ErrorCode.values()){
			if(errorCode.getCode().equals(code)){
				return errorCode.getDesc();
			}
		}
		return "네크워크상태가 불안합니다.";
	}
	
	// --------------
	// default Image
	// --------------
	public static Bitmap getNoBitmap(Context context) {
		return BitmapFactory.decodeResource(context.getResources(), R.drawable.noimg_listposter);
	}

	public static Bitmap getAdultBitmap(Activity activity) {
		return BitmapFactory.decodeResource(activity.getResources(), R.drawable.posterbg_list_19);
	}
	
	// ----------------------
	// drawable resource id
	// ----------------------
	public static int getGrade(String grade) {
		int resId = R.drawable.ageall;
		if(grade.contains("12")) resId = R.drawable.age12;
		if(grade.contains("15")) resId = R.drawable.age15;
		if(grade.contains("19")) resId = R.drawable.age19;
		
		return resId;
	}
	public static boolean isAdultGrade(String grade) {
		if(grade.contains("19")) return true;
		else return false;
	}
	
	// --------------
	// keyboard
	// --------------
	public static void showSoftKeyboard(Context context, EditText editText) {
		InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
	}

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
	}
	
	
	// ---------------------------------------------------------
	// preference
	// ---------------------------------------------------------
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

	// 임시용 네트워크 상태 보여주는 팝업 --> 공통 팝업창으로 교체???
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
	
	// 네트워크 연결 상태 체크
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
