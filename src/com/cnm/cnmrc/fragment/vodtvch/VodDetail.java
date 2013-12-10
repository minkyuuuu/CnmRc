/*
 * Copyright (C) 2011 Chris Gao <chris@exina.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cnm.cnmrc.fragment.vodtvch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.search.SearchMain;
import com.cnm.cnmrc.popup.PopupGtvNotAlive;
import com.cnm.cnmrc.popup.PopupGtvNotAliveTv;
import com.cnm.cnmrc.provider.CnmRcContract.SearchWord;
import com.cnm.cnmrc.provider.CnmRcContract.VodJjim;
import com.cnm.cnmrc.tcp.TCPClientTv;
import com.cnm.cnmrc.tcp.TCPClientVod;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.Util;

public class VodDetail extends Base implements View.OnClickListener {
	static Context mContext;
	String vodAssetId = "";

	public VodDetail newInstance(int selectedCategory, String title, boolean isFirstDepth, Bundle bundle) {
		VodDetail f = new VodDetail();
		Bundle args = new Bundle();
		args.putBoolean("isFirstDepth", isFirstDepth);
		args.putBundle("bundle", bundle);
		f.setArguments(args);
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.search_vod_detail, container, false);

		mContext = getActivity();

		isFirstDepth = getArguments().getBoolean("isFirstDepth");
		Bundle bundle = getArguments().getBundle("bundle");

		vodAssetId = bundle.getString("vodAssetId");
		byte[] logoImage = bundle.getByteArray("logoImg");
		String title = bundle.getString("title");
		String hd = bundle.getString("hd");
		String grade = bundle.getString("grade");
		String director = bundle.getString("director");
		String actor = bundle.getString("actor");
		String price = bundle.getString("price");
		String contents = bundle.getString("contents");

		Bitmap bmp = BitmapFactory.decodeByteArray(logoImage, 0, logoImage.length);
		ImageView logoImg = (ImageView) layout.findViewById(R.id.logo_img);
		logoImg.setImageBitmap(bmp);

		if (title != null)
			((TextView) layout.findViewById(R.id.title)).setText(title);
		if (hd != null) {
			if (hd.equalsIgnoreCase("yes"))
				((ImageView) layout.findViewById(R.id.hd_icon)).setVisibility(View.VISIBLE);
		}
		if (grade != null)
			((ImageView) layout.findViewById(R.id.grade_icon)).setBackgroundResource(Util.getGrade(grade));
		if (director != null)
			((TextView) layout.findViewById(R.id.director_name)).setText(" " + director);
		if (actor != null)
			((TextView) layout.findViewById(R.id.actor_name)).setText(" " + actor);
		if (grade != null)
			((TextView) layout.findViewById(R.id.grade_text)).setText(" " + grade);
		if (price != null)
			((TextView) layout.findViewById(R.id.price_amount)).setText(" " + price);
		if (contents != null)
			((TextView) layout.findViewById(R.id.contents)).setText(contents);

		// vod 찜하기
		ImageButton vod = (ImageButton) layout.findViewById(R.id.vod_zzim);
		vod.setOnClickListener(this);

		// tv에서 시청하기
		ImageButton tv = (ImageButton) layout.findViewById(R.id.vod_tv_watching);
		tv.setOnClickListener(this);

		return layout;
	}

	public boolean allowBackPressed() {
		return true;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		// ----------------------------------------------------------------------------------------------
		// vod에 일부인 vodtopmenu fragment는 vod가 destory될 때 자식인 vodtopmenu도 같이 destory해주진 않는다.
		// 이유는 아마 자식이래도 같은 fragment 레벨이므로 vod의 생명주기와 함께 하는것이 아니라 Activity의 관리를 받는것 같다.
		// ----------------------------------------------------------------------------------------------
		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("search_vod_detail");
			if (f != null) {
				getActivity().getSupportFragmentManager().beginTransaction().remove(f).commit();
			}
		}

		{
			Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(((MainActivity) getActivity()).TAG_FRAGMENT_SEARCH);
			if (f != null) {
				((SearchMain) f).resetTitle();
			}
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	VodJjimSendMassgeHandler mVodJjimMainHandler = null;
	TvSendMassgeHandler mTvMainHandler = null;
	CnmPreferences pref;
	String hostAddress;
	boolean startJobVodJjim = false;
	boolean startJobTv = false;
	boolean vodJjimFromDb = false;	// db에 저장된것을 보내는지 체크...

	@Override
	public void onClick(View v) {
		// sidebar가 열려있으면 return한다.
		// 2013-12-06 comment할려는데 이미 되어있다.
		// ㅎㅎ
		//		if (UiUtil.isSlidingMenuOpening(getActivity()))
		//			return;

		switch (v.getId()) {
		case R.id.vod_zzim:
			// test wifi status
			//boolean b = Util.setWifiEnable(getActivity(), false);

			// wifi check
			if (!Util.isWifiAvailable(getActivity())) {
				Toast.makeText(getActivity(), "WiFi not Available", Toast.LENGTH_SHORT).show();
				break;
			}

			// STB alive check
			pref = CnmPreferences.getInstance();
			hostAddress = pref.loadPairingHostAddress(getActivity());

			// test
			//hostAddress = "192.168.0.6";

			try {
				if (hostAddress.equals("")) {
					// 보낼수 없으면 10개까지만 저장한다.
					Log.d("hwang", "vod 찜 : db insert");
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					PopupGtvNotAlive gtvNotAlive = PopupGtvNotAlive.newInstance(vodAssetId);
					gtvNotAlive.show(ft, PopupGtvNotAlive.class.getSimpleName());
					break;
				} else {

					InetAddress address = InetAddress.getByName(hostAddress);
					boolean alive = address.isReachable(2000);

					if (alive) {
						if (startJobVodJjim) {
							Log.d("hwang", "don't send again");
							break; // 현재 화면에서 한번 보내고 나면 다시 보내지 말자!!!
						}
						startJobVodJjim = true;

						Log.d("hwang", "send vod 찜");
						mVodJjimMainHandler = new VodJjimSendMassgeHandler();

						// if 0, just send
						Cursor cursor = getActivity().getContentResolver().query(VodJjim.CONTENT_URI, null, null, null, null);
						if (cursor != null) {
							if (cursor.getCount() == 0) {
								// 하나만 보낸다.
								vodJjimFromDb = false;
								TCPClientVod tcpVodClient = new TCPClientVod(mVodJjimMainHandler, hostAddress, vodAssetId);
								tcpVodClient.start();

							} else { // 일괄처리
								// prepairing for send-loop
								vodJjimFromDb = true;
								getActivity().getContentResolver().registerContentObserver(VodJjim.CONTENT_URI, true, observer);

								// db insert
								Util.insertDBVodJjim(getActivity(), vodAssetId); // db insert

							}
						}
						cursor.close();
						break;
					} else {
						// 보낼수 없으면 10개까지만 저장한다.
						Log.d("hwang", "vod 찜 : db insert");
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						PopupGtvNotAlive gtvNotAlive = PopupGtvNotAlive.newInstance(vodAssetId);
						gtvNotAlive.show(ft, PopupGtvNotAlive.class.getSimpleName());
						break;
					}
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;

		case R.id.vod_tv_watching:
			// wifi check
			if (!Util.isWifiAvailable(getActivity())) {
				Toast.makeText(getActivity(), "WiFi not Available", Toast.LENGTH_SHORT).show();
				break;
			}

			// STB alive check
			pref = CnmPreferences.getInstance();
			hostAddress = pref.loadPairingHostAddress(getActivity());

			// test
			//hostAddress = "192.168.0.6";

			try {
				if (hostAddress.equals("")) {
					FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
					PopupGtvNotAliveTv gtvNotAlive = new PopupGtvNotAliveTv();
					gtvNotAlive.show(ft, PopupGtvNotAliveTv.class.getSimpleName());
					break;
				} else {

					InetAddress address = InetAddress.getByName(hostAddress);
					boolean alive = address.isReachable(2000);
					if (alive) {
						if (startJobTv) {
							Log.d("hwang", "don't send again");
							break; // 현재 화면에서 한번 보내고 나면 다시 보내지 말자!!!
						}
						startJobTv = true;

						mTvMainHandler = new TvSendMassgeHandler();

						TCPClientTv tcpTvClient = new TCPClientTv(mTvMainHandler, hostAddress, vodAssetId);
						tcpTvClient.start();
						break;
					} else {
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						PopupGtvNotAliveTv gtvNotAlive = new PopupGtvNotAliveTv();
						gtvNotAlive.show(ft, PopupGtvNotAlive.class.getSimpleName());
						break;

					}
				}
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		}
	}

	// Handler 클래스
	class VodJjimSendMassgeHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 1:
				
				//Log.d("hwang", "from server : " + ((String) msg.obj).substring(0, 9) + " <--- " + System.currentTimeMillis());
				if (startJobVodJjim) {
					// substring(4, 8); 4byte CM01  / substring(8, 9); 0성공
					// return correct / popup 띄우기 / after unregister, db 지우기 /일단 일괄처리를 하자. / 
					// getActivity().getContentResolver().delete(VodJjim.CONTENT_URI, null, null);
					// return incorrect / startJobVodJjim = false; / popup 띄우기 /
					String trNo = ((String) msg.obj).substring(4, 8);	// 4 byte
					String result = ((String) msg.obj).substring(8, 9);	// 1 byte
					Log.d("hwang", "(startJobVodJjim)from server trNo: " + trNo + " <--- " + System.currentTimeMillis());
					Log.d("hwang", "(startJobVodJjim)from server result : " + result + " <--- " + System.currentTimeMillis());
					
					// 원칙적으로 개별적으로 지워야 하는데... 그럴려면 서버에서 성공처리한 assetid를 보내주어야한다. 
					// 지금은 한번만 성공하면 모두 성공처리한것으로 간주하고 일괄 delete한다.
					if (trNo.equals("CM01") && result.equals("0")) {
						if (vodJjimFromDb)
							getActivity().getContentResolver().delete(VodJjim.CONTENT_URI, null, null);
					} else {
						startJobVodJjim = false;
					}
					
				}
			}
		}
	};
	
	class TvSendMassgeHandler extends Handler {
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 1:
				if (startJobTv) {
					// substring(4, 8); 4byte CM02 / / substring(8, 9); 0성공
					// return correct / popup 띄우기 
					// return incorrect / startJobVodJjim = false; / popup 띄우기 /
					String trNo = ((String) msg.obj).substring(4, 8);	// 4 byte
					String result = ((String) msg.obj).substring(8, 9);	// 1 byte
					Log.d("hwang", "(startJobTv)from server trNo: " + trNo + " <--- " + System.currentTimeMillis());
					Log.d("hwang", "(startJobTv)from server result : " + result + " <--- " + System.currentTimeMillis());
				} else {
					startJobTv = false;
				}
				
				break;
			}
		}
	};

	boolean once = true;
	// vodjjim에서만 사용된다.
	// 10개가 저장된 상태에서 새로운것이 들어오면 delete후 insert가 되면서, 이 때 서버로 보내게 되면 observer가 2번 발생되어
	// 10개씩 두번 보내게 된다. 그래서 한번만 보내자... 현재 화면에서는 한 번만 보내게끔 되어 있다. 서버가 alive되어있는 상태에서 보내기 때문이다...
	// 그리구 보내구 나서 db 지울때도 필요하다. 엄밀히 말해서 0개이므로 갯수로 체크해도 된다. 위의 경우도 10개일 떄만???
	// 해당 데이터베이스의 데이터값이 변경시에 onChange() method가 호출됩니다.
	private ContentObserver observer = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			Log.d("hwang", "onChanged : " + selfChange);

			if (once) {
				once = false;
				final Cursor cursor = getActivity().getContentResolver().query(VodJjim.CONTENT_URI, null, null, null, null);

				while (cursor.moveToNext()) {
					TCPClientVod tcpVodClient = new TCPClientVod(mVodJjimMainHandler, hostAddress, cursor.getString(1));
					tcpVodClient.start();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				cursor.close();
			}
		}
	};

	@Override
	public void onDestroy() {
		getActivity().getContentResolver().unregisterContentObserver(observer);
		super.onDestroy();
	}

}
