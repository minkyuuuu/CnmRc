package com.cnm.cnmrc;

import java.io.File;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.cnm.cnmrc.util.CnmPreferences;
import com.cnm.cnmrc.util.Sound;
import com.cnm.cnmrc.util.Util;


/**
 * 
 * App 설치후 처음 진입 했을 때... 해야 할일을 생각해 보자...
 * 
 * 0) 여기서 deviceId는 UDID, IMEI, terminalId를 의미한다.
 * 
 * 1) AuthenticateClient : 클라이언트를 인증하고 termianlKey를 얻는다. 
 *     - 최초 한번, 그러나 정책이 바뀐것 같음, 하지 않는다. 영원히...
 *     - 이 과정에서 서버는 단말기에 대한 로그나 통계 데이타를 만드는 기준을 갖게 되는것 같음.
 *     - terminalId를 요청변수에 넣는데 이는 아마 UDID 일것이다.
 * 
 * 2) CheckRegistUser : 사용회원으로 등록된 사용자인지 여부를 확인한다.
 *    - 기존 서비스되고 있는 CnmTvGuide에서는 처음 App 진입시 체크를 하는데...
 *    - 아무런 의미가 없는것 같다.
 *    - 사용회원이면 CnmTvGuide에서는 셋탑이 등록되었다고 처리한다.
 *    - setSettopRegAuth(true) 처리한다.
 *    
 * 3) 2)번경우 이전에 이미 ClientSetTopBoxRegist 인증이 이루어졌다는 의미이다.
 *    - ClientSetTopBoxRegist.asp?deviceId=%s&authKey=%s
 *    
 *
 */

public class CnmApplication extends Application {

	CnmPreferences pref;
	
	Sound mSoundPool;

	@Override
	public void onCreate() {
		super.onCreate();

		// -----------------------------------------------------------------------------------
		// App 설치후 처음 진입 했을 때... 해야 할일...
		// 1) AuthenticateClient : terminalID 발급을 받아야 할것 같은데... 기존 앱에서는 발급받지 않는다.
		//    발급 받지 않는 대신에 지금 만들 앱에서는 제공되는 안드로이용 termianlID를 사용한다.
		//    : MjAxMS0wNC0xNl80OTk1NTY4NF9Dbk1UZXN0QW5kcm9pZF8g (from 조규상 to 박종필)
		// 2) CheckRegistUser을 해서 등록된 사용자이면 셋탑등록이 되었다고 설정한다.
		//    : 새로운 앱에서는 기존앱의 셋탑등록 여부에 관심갖을 필요가 없다.
		// 3) 따라서 처음 앱 진입시 해주야 할 일은...
		//    1. 의례적인 첫 앱 진입인지?
		//    2. 설정에 사용되는 기본값을 저장한다.
		// -----------------------------------------------------------------------------------
		
		pref = CnmPreferences.getInstance();
		
		mSoundPool = new Sound(getApplicationContext());
		
		// 앱 진입시 마다 cache folder 삭제
//		Log.e("hwang", "starting remove files on cache");
//		File mkDir = null;
//		if (getApplicationContext() != null) {
//			mkDir = getApplicationContext().getExternalCacheDir();
//		} 		
//		if (mkDir == null) {
//			mkDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), Util.PACKGE_NAME);
//		} 
//		
//		if(mkDir.exists()) {
//			Util.removeDIR(mkDir.toString());
//			Log.e("hwang", "remove files on cache");
//		}
		
		// App 설치후 처음 진입인지?
		if(pref.loadFirstLoadingCnmApp(getApplicationContext())) {
			pref.saveFirstLoadingCnmApp(getApplicationContext(), false);
			pref.saveTerminalKey(getApplicationContext(), "MjAxMS0wNC0xNl80OTk1NTY4NF9Dbk1UZXN0QW5kcm9pZF8g");
			setMobileIMIE(getApplicationContext());
			
			initializeConfig(getApplicationContext());
		}
		
	}

	private void initializeConfig(Context context) {

	}

	public void setMobileIMIE(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String UDID = telephonyManager.getDeviceId();

		if (UDID != null) {
			pref.saveUdid(getApplicationContext(), UDID);
		} else {
			WifiManager wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wfManager.getConnectionInfo();
			UDID = wifiInfo.getMacAddress().replace(":", "");
			pref.saveUdid(getApplicationContext(), UDID);
		}
	}
	

}
