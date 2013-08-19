package com.cnm.cnmrc.config;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.data.ItemConfigRegion;

public class ConfigRegion extends Fragment implements View.OnClickListener {

	public static ConfigRegion newInstance(String type) {
		ConfigRegion f = new ConfigRegion();
		Bundle args = new Bundle();
		args.putString("type", type); // vod or tvch
		f.setArguments(args);
		return f;
	}

	LinearLayout mConfigRegion; // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.

	ListView mListView;
	ArrayList<ItemConfigRegion> arrayList = null;
	ConfigRegionAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_region, container, false);

		mConfigRegion = (LinearLayout) layout.findViewById(R.id.config_region);
		mConfigRegion.setOnClickListener(this);

		// listview
		mListView = (ListView) layout.findViewById(R.id.config_region_listview);

		// make item data
		arrayList = new ArrayList<ItemConfigRegion>(20);
		for (int i = 0; i < 20; i++) {
			ItemConfigRegion item = new ItemConfigRegion();

			item.setRegion("강남");
			item.setGu("강남구");

			arrayList.add(item);
		}

		// -------------------
		// region
		// -------------------
		mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		adapter = new ConfigRegionAdapter(getActivity(), R.layout.list_item_config_region, arrayList);
		mListView.setAdapter(adapter);
		mListView.setDivider(null);
		mListView.setDividerHeight(0);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// RadioButton radio =
				// (RadioButton)view.findViewById(R.id.config_region_radio);
				// radio.setChecked(true);

				adapter.setSelectedIndex(position);
				adapter.notifyDataSetChanged();

				showProduct();
			}

		});

		return layout;
	}

	private void showProduct() {
		Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag("config_region");
		if (f != null) {
			ConfigProduct configRegion = ConfigProduct.newInstance("강남");
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

			// ft.replace전에 animation을 설정해야 한다.
			ft.setCustomAnimations(R.anim.vod_tvch_base_entering, 0);
			ft.add(R.id.config_panel, configRegion, "config_product");
			ft.addToBackStack(null); // fragment stack에 넣지 않으면 백키가 activity
										// stack에 있는걸 처리한다. 즉 여기서는 앱이 종료된다.
			ft.commit();
			getActivity().getSupportFragmentManager().executePendingTransactions();
		}

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
//		switch (v.getId()) {
//		//case R.id.config: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
//			break;
//		}

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
