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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.fragment.search.SearchMain;
import com.cnm.cnmrc.tcp.TCPClientTv;
import com.cnm.cnmrc.tcp.TCPClientVod;
import com.cnm.cnmrc.util.CnmPreferences;
import com.cnm.cnmrc.util.UiUtil;
import com.cnm.cnmrc.util.Util;
import com.google.android.apps.tvremote.RemoteDevice;

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

	SendMassgeHandler mMainHandler = null;
	CnmPreferences pref;
	String hostAddress;
	@Override
	public void onClick(View v) {
		// sidebar가 열려있으면 return한다.
		if (UiUtil.isSlidingMenuOpening(getActivity()))
			return;
		
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
			//hostAddress = "192.168.0.9";
			
			try {
				if (hostAddress.equals("")) {
					Toast.makeText(getActivity(), "Not connect STB", Toast.LENGTH_SHORT).show();
					break;
				} else {
					
					InetAddress address = InetAddress.getByName(hostAddress);
					boolean alive = address.isReachable(2000);
					if (alive) {
						mMainHandler = new SendMassgeHandler();
						
						TCPClientVod tcpVodClient = new TCPClientVod(mMainHandler, hostAddress, vodAssetId);
						tcpVodClient.start();
						
						break;
					} else {
						Toast.makeText(getActivity(), "Not connect STB", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getActivity(), "Not connect STB", Toast.LENGTH_SHORT).show();
					break;
				} else {
					
					InetAddress address = InetAddress.getByName(hostAddress);
					boolean alive = address.isReachable(2000);
					if (alive) {
						mMainHandler = new SendMassgeHandler();
						
						TCPClientTv tcpTvClient = new TCPClientTv(mMainHandler, hostAddress, vodAssetId);
						tcpTvClient.start();
						break;
					} else {
						Toast.makeText(getActivity(), "Not connect STB", Toast.LENGTH_SHORT).show();
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
    class SendMassgeHandler extends Handler {
         
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             
            switch (msg.what) {
            case 1:
            	Toast.makeText(mContext, (String)msg.obj, Toast.LENGTH_LONG).show();
                break;
 
            default:
                break;
            }
        }
         
    };

}
