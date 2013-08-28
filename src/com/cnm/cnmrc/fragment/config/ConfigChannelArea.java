package com.cnm.cnmrc.fragment.config;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cnm.cnmrc.R;
import com.cnm.cnmrc.parser.ChannelArea;
import com.cnm.cnmrc.parser.ChannelAreaParser;
import com.cnm.cnmrc.util.Util;

public class ConfigChannelArea extends Fragment implements View.OnClickListener {

	public static ConfigChannelArea newInstance(String type) {
		ConfigChannelArea f = new ConfigChannelArea();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout noClickBelowLayout; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	ListView listView;
	ConfigAreaAdapter adapter = null;

	ArrayList<ChannelArea> mResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_area, container, false);

		noClickBelowLayout = (LinearLayout) layout.findViewById(R.id.area_no_click_below_layout);
		noClickBelowLayout.setOnClickListener(this);

		// listview
		listView = (ListView) layout.findViewById(R.id.config_area_listview);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setDivider(null);
		listView.setDividerHeight(0);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//((ListView) parent).setItemChecked(position, true);
				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();

				gotoProduct(mResult.get(position).getAreaCode(), mResult.get(position).getAreaName());
			}

		});
		//if(adapter != null) adapter.notifyDataSetChanged();

		return layout;
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		Animation anim;

		if (enter)
			anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_entering);
		else
			anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_exit);

		anim.setAnimationListener(new AnimationListener() {
			public void onAnimationStart(Animation animation) {
			}

			public void onAnimationEnd(Animation animation) {
				showArea();
			}

			public void onAnimationRepeat(Animation animation) {
			}
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
			ChannelAreaParser areaParser = new ChannelAreaParser();
			areaParser.start();

			return areaParser.getList().getList();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ArrayList<ChannelArea> result) {
			mResult = result;
			adapter = new ConfigAreaAdapter(getActivity(), R.layout.list_item_config_area, result);
			listView.setAdapter(adapter);
			//setItemChecked();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	private void gotoProduct(String areaCode, String areaName) {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("config_area");
		if (f != null) {
			ConfigChannelProduct configRegion = ConfigChannelProduct.newInstance(areaCode, areaName);
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

			// ft.replace전에 animation을 설정해야 한다.
			// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
			ft.setCustomAnimations(R.anim.fragment_entering, 0, 0, R.anim.fragment_exit);
			ft.add(R.id.config_panel, configRegion, "config_product");
			ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity
										// stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}

	}
	
	public void setItemChecked() {
		// 저장된 area를 체크한다.
		listView.setItemChecked(getPosition(), true);
	}
	
	public int getPosition() {
		String areaNameText;
		int pos = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			areaNameText = adapter.getItem(i).getAreaName();
            if(Util.getChannelAreaName(getActivity()).replace(" ", "").equals(areaNameText.replace(" ", ""))) {
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
		// switch (v.getId()) {
		// //case R.id.config: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
		// break;
		// }

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
		if(adapter != null) {
			setItemChecked();
			adapter.notifyDataSetChanged();
		}
		super.onResume();
	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

}
