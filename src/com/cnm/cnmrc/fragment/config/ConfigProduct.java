package com.cnm.cnmrc.fragment.config;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.http.ChannelProduct;
import com.cnm.cnmrc.http.ChannelProductParser;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class ConfigProduct extends Fragment implements View.OnClickListener {

	public static ConfigProduct newInstance(String areaCode, String areaName) {
		ConfigProduct f = new ConfigProduct();
		Bundle args = new Bundle();
		args.putString("areaCode", areaCode); // vod or tvch
		args.putString("areaName", areaName); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout preventClickDispatching; 	// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	ListView listView;
	ConfigProductAdapter adapter;
	
	Button done;						// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.
	String selectedAreaCode = null;
	String selectedAreaName = null;
	String selectedProductName = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_product, container, false);

		// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.product_prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.
		done = (Button) layout.findViewById(R.id.config_product_done);
		done.setOnClickListener(this);
		
		// 선택된 area code
		selectedAreaCode = getArguments().getString("areaCode");
		selectedAreaName = getArguments().getString("areaName");
				
		// ---------------------------------------------------------------------
		// listview
		// list item을 custum widget으로 구현 (impelments Clickable)
		// <com.cnm.cnmrc.custom.CheckableLinearLayout />
		// ---------------------------------------------------------------------
		listView = (ListView) layout.findViewById(R.id.config_product_listview);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				RadioButton radio = (RadioButton) view.findViewById(R.id.config_product_radio);
//				radio.setChecked(true);
				
				selectedProductName = adapter.getItem(position).getProductName();
				((ListView) parent).setItemChecked(position, true);  // OK
			}

		});

		return layout;
	}
	
	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim;
		
		if (enter) anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_entering);
		else anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_exit);

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) { }
			public void onAnimationEnd(Animation animation) { 
				((MainActivity)getActivity()).getMyProgressBar().show();
				showProduct(); 
				}
			public void onAnimationRepeat(Animation animation) { }
		});

		return anim;
	}
	
	private void showProduct() {
		//check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity(), "Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		} else {
			String url = UrlAddress.Channel.getGetChannelProduct(selectedAreaCode);
			new ProductAsyncTask().execute(url);
		}
	}

	private class ProductAsyncTask extends AsyncTask<String, Void, ArrayList<ChannelProduct>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<ChannelProduct> doInBackground(String... params) {
			ChannelProductParser parser = new ChannelProductParser(params[0]);
			parser.start();

			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ChannelProduct> result) {
			adapter = new ConfigProductAdapter(getActivity(), R.layout.list_item_config_product, result);
			listView.setAdapter(adapter);
			setItemChecked();
			((MainActivity)getActivity()).getMyProgressBar().dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}
	
	public void setItemChecked() {
		// 저장된 area와 같으면 해당 area의 product를 체크하구...
		// 그 외의 경우는 첫 번째 product를 체크한다.
        if( isSameAreaCode() ) {
        	listView.setItemChecked(getPosition(), true);
        } else {
        	listView.setItemChecked(0, true);
        }
	}
	
	public boolean isSameAreaCode() {
		if (selectedAreaCode != null) {
			if (Util.getChannelAreaCode(getActivity()).equals(selectedAreaCode)) return true;
			else return false;
		}
		return true;
	}
	
	public int getPosition() {
		String productText;
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			productText = adapter.getItem(i).getProductName();
            if(Util.getChannelProductName(getActivity()).replace(" ", "").equals(productText.replace(" ", ""))) {
            	pos = i;
            	break;
            }
		}
		return pos;
	}

	@Override
	public void onDestroyView() {
		
		// 상품정보에 대한 저장을 하지 않았으면, 이전 지역화면의 선택된 아이템을 저장된 내용으로 바꿔주어야함.
		Fragment fg = ((MainActivity)getActivity()).getFragmentConfigArea();
		if(fg != null) {
			((ConfigArea)fg).setItemChecked();
		}
		
		super.onDestroyView();
	}

	public String allowBackPressed() {
		return getArguments().getString("areaCode");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config_product_done:
			Util.setChannelAreaCode(getActivity(), selectedAreaCode);
			Util.setChannelAreaName(getActivity(), selectedAreaName);
			Util.setChannelProductName(getActivity(), selectedProductName);
			
			// 지역, 상품정보를 refresh
			Fragment fg = ((MainActivity)getActivity()).getFragmentConfig();
			if(fg != null) {
				((ConfigFragment)fg).refreshAreaProduct();
			}
			
			getActivity().onBackPressed();
			break;
		}

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle arg0) {

		super.onActivityCreated(arg0);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}
