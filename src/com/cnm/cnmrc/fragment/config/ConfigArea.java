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
import com.cnm.cnmrc.parser.Area;
import com.cnm.cnmrc.parser.AreaParser;
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

	ArrayList<Area> mResult;
	
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
				//((ListView) parent).setItemChecked(position, true); // not OK, cannot change checked when i want to change item
				//adapter.setSelectedIndex(position);	// not OK, cannot change checked when i want to change item
				//RadioButton radio = (RadioButton) view.findViewById(R.id.config_area_radio);
				//radio.setChecked(true);
				//adapter.notifyDataSetChanged();	// Adapter을 수행한다. 
													// 결국 Adapter안에서 원하는 특정(기본 송파) 아이템을 setChecked하면 안된다. 
													// adapter가 만들어지는 시점에서 즉 통신이 끝난후에 checked해야한다.

				// 아직은 지역정보를 저장할 때가 아니구, 상품화면에서 결정된다.
				gotoProduct(mResult.get(position).getAreaCode(), mResult.get(position).getAreaName());
			}

		});
		// if(adapter != null) adapter.notifyDataSetChanged();

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

	private class AreaAsyncTask extends AsyncTask<Void, Void, ArrayList<Area>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Area> doInBackground(Void... params) {
			AreaParser areaParser = new AreaParser();
			areaParser.start();

			return areaParser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<Area> result) {
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
