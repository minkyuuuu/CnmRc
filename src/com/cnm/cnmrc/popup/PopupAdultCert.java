package com.cnm.cnmrc.popup;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cnm.cnmrc.CnmPreferences;
import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.custom.SlideToggleButton;
import com.cnm.cnmrc.item.ItemAdultCert;
import com.cnm.cnmrc.parser.AdultCertParser;
import com.cnm.cnmrc.util.UrlAddress;
import com.cnm.cnmrc.util.Util;

public class PopupAdultCert extends DialogFragment implements View.OnClickListener {
	
	public interface Interceptor {
		public void onReturnVodSemi(boolean isAdultCert);
	}
	
	private Interceptor interceptor;
	
	LinearLayout preventClickDispatching; 	// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
	Button done;
	SlideToggleButton mAutoAdultCert;
	EditText name, socialSecurityNumber;
	CnmPreferences pref;
	Context context;
	
	@Override
	public void onAttach(Activity activity) {
		context = activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int style = DialogFragment.STYLE_NO_TITLE | DialogFragment.STYLE_NORMAL;
		int theme = android.R.style.Theme_Translucent_NoTitleBar;
		setStyle(style, theme);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = (View) inflater.inflate(R.layout.config_adult_cert, container, false);

		pref = CnmPreferences.getInstance();

		// 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
		preventClickDispatching = (LinearLayout) layout.findViewById(R.id.prevent_click_dispatching);
		preventClickDispatching.setOnClickListener(this);

		// 상단 타이틀영역 왼쪽에 위치한 "완료"버튼, 이전 화면으로 이동.
		done = (Button) layout.findViewById(R.id.adult_cert_done);
		done.setOnClickListener(this);

		name = (EditText) layout.findViewById(R.id.edit_name);
		socialSecurityNumber = (EditText) layout.findViewById(R.id.edit_social_security_number);
		socialSecurityNumber.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					// 이름 입력
					if (name.getText().toString().equals("")) {
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						PopupConfigAdultName popup = new PopupConfigAdultName();
						popup.show(ft, PopupConfigAdultName.class.getSimpleName());
						return true;
					}

					// 주민번호입력, 13자리입력
					if (socialSecurityNumber.getText().toString().equals("")) {
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						PopupConfigAdultNumber popup = new PopupConfigAdultNumber();
						popup.show(ft, PopupConfigAdultNumber.class.getSimpleName());
						return true;
					}

					// 주민번호입력, 13자리입력
					if (socialSecurityNumber.getText().length() != 13) {
						FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
						PopupConfigAdultNumber1 popup = new PopupConfigAdultNumber1();
						popup.show(ft, PopupConfigAdultNumber1.class.getSimpleName());
						return true;
					}
					
					// 성인인증 절차
					getAdultAuthentication(name.getText().toString(), socialSecurityNumber.getText().toString());
				}

				return false;
			}
		});

		// 자동성인인증하기 슬라이딩 토글 버튼
		mAutoAdultCert = (SlideToggleButton) layout.findViewById(R.id.slide_auto_adult_cert);
		mAutoAdultCert.setOnCheckChangedListner(slideToogleButtonListener);
		mAutoAdultCert.setChecked(pref.loadConfigAutoAdultCert(getActivity()));

		return layout;
	}
	
	private SlideToggleButton.OnCheckChangedListner slideToogleButtonListener = new SlideToggleButton.OnCheckChangedListner() {
		@Override
		public void onCheckChanged(View v, boolean isChecked) {
			if (v instanceof SlideToggleButton) {
				if (v.getTag().equals("slide_auto_adult_cert")) {
					if (isChecked) {
						// 자동성인인증이 on일때는 문구는 don't care, 그러나 성인인증화면에는 적용해야한다.
						pref.saveConfigAutoAdultCert(getActivity(), true);
					} else {
						// 자동성인인증이 off가 되면 무조건 성인인증문구는 "미인증상태입니다"로 변경되어야한다. 이 의미는 컨텐츠를 볼 때 무조건 성인인증 절차가 필요하다는 의미.
						pref.saveConfigAutoAdultCert(getActivity(), false);
					}
				}
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.config_prevent_click_dispatching: // 현재화면 밑에있는 화면으로 클릭이벤트가 전달되는것을 막기위함.
			break;
		case R.id.adult_cert_done:
			// 이름 입력
			if (name.getText().toString().equals("")) {
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				PopupConfigAdultName popup = new PopupConfigAdultName();
				popup.show(ft, PopupConfigAdultName.class.getSimpleName());
				break;
			}

			// 주민번호입력, 13자리입력
			if (socialSecurityNumber.getText().toString().equals("")) {
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				PopupConfigAdultNumber popup = new PopupConfigAdultNumber();
				popup.show(ft, PopupConfigAdultNumber.class.getSimpleName());
				break;
			}

			// 주민번호입력, 13자리입력
			if (socialSecurityNumber.getText().length() != 13) {
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				PopupConfigAdultNumber1 popup = new PopupConfigAdultNumber1();
				popup.show(ft, PopupConfigAdultNumber1.class.getSimpleName());
				break;
			}

			// 성인인증 절차
			getAdultAuthentication(name.getText().toString(), socialSecurityNumber.getText().toString());

			break;
		}

	}
	
	private void getAdultAuthentication(String name, String number) {
		// check network and data loading
		if (Util.GetNetworkInfo(getActivity()) == 99) {
			Util.AlertShow(getActivity());
		} else {
			String url = UrlAddress.Authenticate.getAuthenticateAdult(number, name);
			new AdultAuthenticationAsyncTask().execute(url);
		}
	}

	private void showResult() {
		if (mItem.getResultCode().equalsIgnoreCase("ok")) {
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			PopupConfigAdultCertSucceed popup = new PopupConfigAdultCertSucceed();
			popup.show(ft, PopupConfigAdultCertSucceed.class.getSimpleName());

			// 돌아가기전..
			pref.saveConfigAdultCertStatus(getActivity(), true);

			popup.setInterceptor(new PopupConfigAdultCertSucceed.Interceptor() {
				@Override
				public void onGoToConfigMain() {
					if(pref.loadConfigAdultCertStatus(getActivity()) && 
							pref.loadConfigAutoAdultCert(getActivity())) {
						pref.saveConfigAdultCertStatus(getActivity(), true);
						interceptor.onReturnVodSemi(true);
					} else {
						pref.saveConfigAdultCertStatus(getActivity(), false);
						interceptor.onReturnVodSemi(false);
					}
					
					// 나중에 다시 체크...
//					interceptor.onReturnVodSemi(true);	// 이경우는 어쩌든 한 번은 인증성공으로 보고 화면을 갱신한다. 단 설정에는 인증성공이 저장되지는 않는다.
					dismiss();
				}
			});
		} else {
			FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
			PopupConfigAdultCertFail popup = new PopupConfigAdultCertFail();
			popup.show(ft, PopupConfigAdultCertFail.class.getSimpleName());
			
			// 돌아가기전..
			//pref.saveConfigAdultCertStatus(getActivity(), false);
			
			popup.setInterceptor(new PopupConfigAdultCertFail.Interceptor() {
				@Override
				public void onGoToConfigMain() {
//					dismiss();
//					interceptor.onReturnVodSemi(false);
				}
			});
		}

		Log.v("hwang", "result code : " + mItem.getResultCode());
		Log.v("hwang", "error string : " + mItem.getErrString());
	}

	ItemAdultCert mItem;

	private class AdultAuthenticationAsyncTask extends AsyncTask<String, Void, ItemAdultCert> {
		@Override
		protected void onPreExecute() {
			((MainActivity) getActivity()).getMyProgressBar().show();
			super.onPreExecute();
		}

		@Override
		protected ItemAdultCert doInBackground(String... params) {
			AdultCertParser parser = new AdultCertParser(params[0]);
			parser.start();

			return parser.getItem();
		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(ItemAdultCert item) {
			mItem = item;
			((MainActivity) getActivity()).getMyProgressBar().dismiss();
			showResult();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	public void setInterceptor(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

}
