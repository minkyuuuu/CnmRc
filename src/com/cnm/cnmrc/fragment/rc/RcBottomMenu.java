
package com.cnm.cnmrc.fragment.rc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnm.cnmrc.R;

/**
 * 
 * @author minkyu
 * @date 2013. 7. 16.
 */
public class RcBottomMenu extends Fragment implements View.OnClickListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.rc_bottom_menu, container, false);

		return layout;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onClick(View v) {}

}
