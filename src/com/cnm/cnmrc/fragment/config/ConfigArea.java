package com.cnm.cnmrc.fragment.config;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cnm.cnmrc.CnmApplication;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.http.ChannelArea;
import com.cnm.cnmrc.http.ChannelAreaParser;
import com.cnm.cnmrc.util.Util;

public class ConfigArea extends Fragment implements View.OnClickListener {

	public static ConfigArea newInstance(String type) {
		ConfigArea f = new ConfigArea();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout preventClickDispatching; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	
	ListView listView;
	ConfigAreaAdapter adapter = null;
	ArrayList<ChannelArea> mResult;
	
	/**
	 * 통신 모듈의 명명 규칙
	 * 1) 파서관련 클래스 이름은 프로토콜문서의 Api이름을 기준으로 만든다.
	 * 2) 통신모듈을 사용하는 Activity나 Fragment의 클래스이름은 이미 화면구성과 기능에 따라 이름이 정해져있다. (통신모듈과 이름이 같을 수 있는데...)
	 * 3) 파서관련 클래스의 내부 필드명은 프로토콜문서의 elements 이름을 기준으로 한다. 어차피 해당프로토콜를 의미하는 단어는 생략한다. (예, VOD_ID --> ID)
	 *    : 이름은 첫글자는 소문자로 하고 다음 단어는 시작 글자만 대문자로 한다. "_"은 사용하지 않는다. (예, areaCode)
	 *    : element이름과 연관된 string 상수는 모두 대문자로 하고 단어와 단어사이는 "_"을 사용한다. (예, AREA_CODE)
	 */
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_area, container, false);

		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.area_prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);
		
		// ----------------------------------------------------------------------------
		// listview
		// list item을 custum widget으로 구현 (impelments Clickable)
		// <com.cnm.cnmrc.custom.CheckableLinearLayout />
		// list item을 그냥 순수하게 구현할려면 adapter안에서 checked해야하는것 같은데... 잘 안된다.
		// adapter.setSelectedIndex()
		// ----------------------------------------------------------------------------
		listView = (ListView) layout.findViewById(R.id.config_area_listview);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// 아직은 지역정보를 저장할 때가 아니구, 상품화면에서 결정된다.
				gotoProduct(mResult.get(position).getAreaCode(), mResult.get(position).getAreaName());
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
				showArea(); 
			}
			public void onAnimationRepeat(Animation animation) { }
		});

		return anim;
	}

	private void showArea() {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity(), "Wifi 혹은 3G망이 연결되지 않았거나 원활하지 않습니다.네트워크 확인후 다시 접속해 주세요!");
		} else {
			new AreaAsyncTask().execute();
		}
	}

	private class AreaAsyncTask extends AsyncTask<Void, Void, ArrayList<ChannelArea>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<ChannelArea> doInBackground(Void... params) {
			ChannelAreaParser parser = new ChannelAreaParser();
			parser.start();

			return parser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ChannelArea> result) {
			mResult = result;
			adapter = new ConfigAreaAdapter(getActivity(), R.layout.list_item_config_area, result);
			listView.setAdapter(adapter);
			setItemChecked();
			((MainActivity)getActivity()).getMyProgressBar().dismiss();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	private void gotoProduct(String areaCode, String areaName) {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("config_area");
		if (f != null) {
			ConfigProduct configProduct = ConfigProduct.newInstance(areaCode, areaName);
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

			// ft.replace전에 animation을 설정해야 한다.
			// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			ft.setCustomAnimations(R.anim.fragment_entering, 0, 0, R.anim.fragment_exit);
			ft.add(R.id.config_panel, configProduct, "config_product");
			ft.addToBackStack(null); // fragment stack에 넣는다. 즉 activity stack에서
										// 처리한다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}

	}

	public void setItemChecked() {
		// 저장된 area를 체크한다.
		listView.setItemChecked(getPosition(), true);
	}

	public int getPosition() {
		String areaName;
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			areaName = adapter.getItem(i).getAreaName();
			if (Util.getChannelAreaName(getActivity()).replace(" ", "").equals(areaName.replace(" ", ""))) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public String allowBackPressed() {
		return getArguments().getString("type");
	}

	@Override
	public void onClick(View v) {

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
		// if(adapter != null) {
		// setItemChecked();
		// adapter.notifyDataSetChanged();
		// }
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}
	
	Handler handler1 = new Handler();
	
	int count;
	Thread thread = new Thread() {
		public void run() {
			count = 0;
			while (count < 100) {
				try {
					Thread.sleep(100);
				} catch (Exception e) {
				}
//				handler1.post(new Runnable() {
//					public void run() {
//						progressBar.setProgress(count);
//					}
//				});
				count++;
			}
		}
	};


}
