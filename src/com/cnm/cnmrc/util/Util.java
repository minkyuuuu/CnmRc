package com.cnm.cnmrc.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;

@SuppressLint("SimpleDateFormat")
public class Util {

	// 네트워크 접속상태 확인 상수
	public final static int MOBILE_CONNECTED = 0;
	public final static int WIFI_CONNECTED = 1;
	public final static int WIMAX_CONNECTED = 6;
	public final static int DISCONNECTED = 99; // network is not available!!!

	static String[] dayOfWeek = { "", "일", "월", "화", "수", "목", "금", "토" };

	// --------------
	// Date
	// --------------
	public static Date toDate(int yyyy, int mm, int dd) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, yyyy);
		cal.set(Calendar.MONTH, mm);
		cal.set(Calendar.DAY_OF_MONTH, dd);
		return cal.getTime();
	}

	public static Calendar toCalendar(Date solraFixedDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(solraFixedDate);
		return calendar;
	}

	public static Date getFromStringToDate(String string) {
		try {
			java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			java.util.Date date = format.parse(string);
			return date;
		} catch (Exception e) {
			Log.e("hwang", "getSearchProgramDate : " + e.toString());
		}
		return null;
	}

	public static String getSearchProgramDate(Date date) {
		String str;
		str = getMMDD(date);
		str += "(" + getDayOfWeek(date) + ") ";
		str += getHHmm(date);
		return str;
	}

	public static String getMMDD(Date date) {
		return new SimpleDateFormat("MM.dd").format(date);
	}

	public static String getHHmm(Date date) {
		return new SimpleDateFormat("HH:mm").format(date);
	}

	public static String getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int index = c.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek[index];
	}

	// ------------------------
	// AsyncTask onCancelled()
	// ------------------------
	public static void onCancelled(Activity activity) {
		Log.i("hwang", "cancelled!!!");
		String desc = Util.getErrorCodeDesc("999");
		Toast.makeText(activity, desc, Toast.LENGTH_LONG).show();

		((MainActivity) activity).getMyProgressBar().dismiss();
	}

	// ------------------------
	// error code description
	// ------------------------
	public static String getErrorCodeDesc(String code) {
		for (ErrorCode errorCode : ErrorCode.values()) {
			if (errorCode.getCode().equals(code)) {
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

	public final static String PACKGE_NAME = "com.cnm.cnmrc.cache";

	public static Bitmap BitmapLoadFromFile(Context context, String fileName) {

		int lastPos = fileName.lastIndexOf("/");
		int dotPos = fileName.lastIndexOf(".");
		fileName = fileName.substring(lastPos, dotPos);

		File externalCacheDir = context.getExternalCacheDir(); // /storage/sdcard0/Android/data/com.cnm.cnmrc/cache
		String path = null;
		if (externalCacheDir != null) {
			path = externalCacheDir.toString();
		} else {
			path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PACKGE_NAME;
		}

		Bitmap origbmp = null;

		try {
			origbmp = BitmapFactory.decodeFile(path + fileName + "list");
		} catch (Exception e) {
			Log.w("hwang", e.toString());
		}

		return origbmp;
	}

	public static void removeDIR(String source) {
		File[] listFile = new File(source).listFiles();
		try {
			if (listFile.length > 0) {
				for (int i = 0; i < listFile.length; i++) {
					if (listFile[i].isFile()) {
						listFile[i].delete();
					} else {
						removeDIR(listFile[i].getPath());
					}
					listFile[i].delete();
				}
			}
		} catch (Exception e) {
			System.err.println(System.err);
			System.exit(-1);
		}

	}

	// ----------------------
	// drawable resource id
	// ----------------------
	public static int getGrade(String grade) {
		int resId = R.drawable.ageall;
		if (grade.contains("12"))
			resId = R.drawable.age12;
		if (grade.contains("15"))
			resId = R.drawable.age15;
		if (grade.contains("19"))
			resId = R.drawable.age19;

		return resId;
	}

	public static boolean isAdultGrade(String grade) {
		if (grade.contains("19"))
			return true;
		else
			return false;
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

	public static String getChannelProductCode(Context context) {
		CnmPreferences pref = CnmPreferences.getInstance();
		return pref.loadChannelProductCode(context);
	}

	public static void setChannelProductCode(Context context, String value) {
		CnmPreferences pref = CnmPreferences.getInstance();
		pref.saveChannelProductCode(context, value);
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
	public static void AlertShow(Context context) {
		String msg = context.getResources().getString(R.string.network_warning);

		AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
		alert_internet_status.setTitle("경 고");
		alert_internet_status.setMessage(msg);
		alert_internet_status.setPositiveButton("닫 기", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss(); // 닫기
				// finish();
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
			case ConnectivityManager.TYPE_MOBILE:  // 3G (0)
				return MOBILE_CONNECTED;
			case ConnectivityManager.TYPE_WIFI:  // Wi-Fi (1)
				return WIFI_CONNECTED;
			case ConnectivityManager.TYPE_WIMAX:  // 4G (6)
				return WIMAX_CONNECTED;
			}
		}
		return DISCONNECTED;
	}

	public static String getURLEncoder(String search) {
		try {
			return URLEncoder.encode(search, "UTF8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
}
