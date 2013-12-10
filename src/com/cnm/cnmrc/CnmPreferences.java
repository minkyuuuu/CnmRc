package com.cnm.cnmrc;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;

public class CnmPreferences {
	// Preference Define
	public final String CNM_PREFERENCE 			= "com.cnm.cnmrc";
	int mode = Activity.MODE_PRIVATE;
	
	// Key Define
	public final String FIRST_LOADING_CNM_APP = "first_loading_cnm_app";
	public final String FIRST_CONNECT_GTV = "first_connect_gtv";
	public final String CHECK_REGIST_USER = "check_regist_user";	// no needed in this App
	
	public final String VERSION = "version";
	public final String TRANSACTION_ID = "transaction_id";
	public final String TERMINAL_KEY = "terminal_key";
	
	public final String UDID = "udid";									// Unique Device Identifier
	
	public final String CHANNEL_AREA_CODE = "channel_area_code";		// Channel Area Code
	public final String CHANNEL_AREA_NAME = "channel_area_name";		// Channel Area Name
	public final String CHANNEL_PRODUCT_CODE = "channel_product_code";	// Channel Product Code
	public final String CHANNEL_PRODUCT_NAME = "channel_product_name";	// Channel Product Name
	
	public final String CONFIG_VIBRATE_EFFECT = "config_vibrate_effect";		// 설정 (진동효과)
	public final String CONFIG_SOUND_EFFECT = "config_vibrate_effect";			// 설정 (소리효과)
//	public final String CONFIG_TOUCH_SPEED = "config_touch_speed";				// 설정 (터치속도)
	public final String CONFIG_VOD_UPDATE_NOTI = "config_vod_update_noti";		// 설정 (VOD업데이트알림)
	public final String CONFIG_WATCHING_RES_NOTI = "config_watching_res_noti";	// 설정 (시청예약알림)
	public final String CONFIG_AUTO_ADULT_CERT = "config_auto_adult_cert";		// 설정 (자동성인인증하기)
	public final String CONFIG_ADULT_CERT_STATUS = "config_adult_cert_status";	// 설정 (성인인증상태)
	
	public final String PAIRING_HOST_ADDRESS = "pairing_host_address";			// pairing되는 Google STB host address (예, 192.168.0.25)

	private static CnmPreferences instance;

	public static CnmPreferences getInstance() {
		if (instance == null)
			instance = new CnmPreferences();
		return instance;
	}
	
	// App 설치후 처음 진입할 때... true, 그 후는 false임. -------------------------------------------
	public void saveFirstLoadingCnmApp(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();

		editor.putBoolean(FIRST_LOADING_CNM_APP, value);
		editor.commit();
	}
	public boolean loadFirstLoadingCnmApp(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(FIRST_LOADING_CNM_APP, true);
	}
	
	// App 설치후 Gtv 연결을 시도했느냐? 기본 false, 시도한 후는 true -------------------------------------------
	public void saveFirstConnectGtv(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(FIRST_CONNECT_GTV, value);
		editor.commit();
	}
	public boolean loadFirstConnectGtv(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(FIRST_CONNECT_GTV, false);
	}
	
	// transaction id ------------------------------------------------------------------------
	public void saveTransactionId(Context context, int value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();

		editor.putInt(TRANSACTION_ID, value);
		editor.commit();
	}
	public int loadTransactionId(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getInt(TRANSACTION_ID, 1);
	}
	
	// version ------------------------------------------------------------------------
	public void saveVersion(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(VERSION, value);
		editor.commit();
	}
	public String loadVersion(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(VERSION, "SmartMobile_v1.0.0");
	}
	
	// terminal key ------------------------------------------------------------------------
	public void saveTerminalKey(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(TERMINAL_KEY, value);
		editor.commit();
	}
	public String loadTerminalKey(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(TERMINAL_KEY, "MjAxMS0wNC0xNl80OTk1NTY4NF9Dbk1UZXN0QW5kcm9pZF8g");
	}
	
	// udid ------------------------------------------------------------------------
	public void saveUdid(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(UDID, value);
		editor.commit();
	}
	public String loadUdid(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(UDID, "");
	}
	
	// channel area code ----------------------------------------------------------------------
	public void saveChannelAreaCode(Context context, String value) {	// 송파 : 12
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(CHANNEL_AREA_CODE, value);
		editor.commit();
	}
	public String loadChannelAreaCode(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(CHANNEL_AREA_CODE, "12");	
	}
	
	// channel area name ----------------------------------------------------------------------
	public void saveChannelAreaName(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(CHANNEL_AREA_NAME, value);
		editor.commit();
	}
	public String loadChannelAreaName(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(CHANNEL_AREA_NAME, "송파");
	}
	
	// channel product code -------------------------------------------------------------------
	public void saveChannelProductCode(Context context, String value) {	// 송파 디지털기본형 : 12
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(CHANNEL_PRODUCT_CODE, value);
		editor.commit();
	}
	public String loadChannelProductCode(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(CHANNEL_PRODUCT_CODE, "12");
	}
	
	// channel product name -------------------------------------------------------------------
	public void saveChannelProductName(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(CHANNEL_PRODUCT_NAME, value);
		editor.commit();
	}
	public String loadChannelProductName(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(CHANNEL_PRODUCT_NAME, "디지털기본형");
	}
	
	
	
	
	// config vibrate effect -----------------------------------------------------------------
	public void saveConfigVibrateEffect(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_VIBRATE_EFFECT, value);
		editor.commit();
	}
	public boolean loadConfigVibrateEffect(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_VIBRATE_EFFECT, false);
	}
	// config sound effect -----------------------------------------------------------------
	public void saveConfigSoundEffect(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_SOUND_EFFECT, value);
		editor.commit();
	}
	public boolean loadConfigSoundEffect(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_SOUND_EFFECT, false);
	}
	// config touch speed -----------------------------------------------------------------
//	public void saveConfigTouchSpeed(Context context, int value) {
//		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
//		SharedPreferences.Editor editor = pref.edit();
//		
//		editor.putInt(CONFIG_TOUCH_SPEED, value);
//		editor.commit();
//	}
//	public int loadConfigTouchSpeed(Context context) {
//		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
//		return pref.getInt(CONFIG_TOUCH_SPEED, 50);
//	}
	// config vod update notification -----------------------------------------------------
	public void saveConfigVodUpdateNoti(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_VOD_UPDATE_NOTI, value);
		editor.commit();
	}
	
	public boolean loadConfigVodUpdateNoti(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_VOD_UPDATE_NOTI, false);
	}
	// config watch reservation notification ----------------------------------------------
	public void saveConfigTvWatchResNoti(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_WATCHING_RES_NOTI, value);
		editor.commit();
	}
	public boolean loadConfigTvWatchResNoti(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_WATCHING_RES_NOTI, false);
	}
	// config auto adult certification -----------------------------------------------------
	public void saveConfigAutoAdultCert(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_AUTO_ADULT_CERT, value);
		editor.commit();
	}
	public boolean loadConfigAutoAdultCert(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_AUTO_ADULT_CERT, false);
	}
	// config adult certification status-----------------------------------------------------
	public void saveConfigAdultCertStatus(Context context, boolean value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(CONFIG_ADULT_CERT_STATUS, value);
		editor.commit();
	}
	public boolean loadConfigAdultCertStatus(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getBoolean(CONFIG_ADULT_CERT_STATUS, false);	// "미인증 상태입니다."
	}
	
	
	
	
	
	// pairing host address with Google STB -----------------------------------------------------
	public void savePairingHostAddress(Context context, String value) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(PAIRING_HOST_ADDRESS, value);
		editor.commit();
	}
	public String loadPairingHostAddress(Context context) {
		SharedPreferences pref = context.getSharedPreferences(CNM_PREFERENCE, mode);
		return pref.getString(PAIRING_HOST_ADDRESS, "");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}