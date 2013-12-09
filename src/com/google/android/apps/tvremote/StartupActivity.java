/*
 * Copyright (C) 2010 Google Inc.  All rights reserved.
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

package com.google.android.apps.tvremote;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.cnm.cnmrc.MainActivity;
import com.cnm.cnmrc.R;
import com.cnm.cnmrc.util.TimeWatch;

/**
 * Startup activity that checks if certificates are generated, and if not begins
 * async generation of certificates, and displays remote logo.
 * 
 */
public class StartupActivity extends CoreServiceActivity {

	private boolean keystoreAvailable;
	TimeWatch watch;
	ImageView coachMark;

	/**
	 * 주 기능: splash display (1500 milliseconds) make KeyStore at
	 * /data/data/com.cnm.cnmrc/files/ipremote.keystore
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Show splash UI.
		setContentView(R.layout.startup);

		coachMark = (ImageView) findViewById(R.id.coachmark);
		coachMark.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (coachMark.getVisibility() == (View.VISIBLE)) {
					keystoreAvailable = true;
					showMainActivity();
				}
			}
		});

		Log.e("hwang-tvremote", "StartupActivity : onCreate()");
	}

	@Override
	protected void onServiceAvailable(CoreService coreService) {
		if (!getKeyStoreManager().hasServerIdentityAlias()) {
			Log.i("hwang-tvremote", "StartupActivity : start");
			watch = TimeWatch.start();
			new KeystoreInitializerTask(getUniqueId()).execute(getKeyStoreManager()); // take times : 1500 milliseconds
		} else {
			Log.i("hwang-tvremote", "StartupActivity : already KeyStore hasServerIdentityAlias");

			// show MainActiviry after 1500 milliseconds
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					coachMark.setVisibility(View.VISIBLE);
				}
			}, 2000);
		}
	}

	@Override
	protected void onServiceDisconnecting(CoreService coreService) {
		// Do nothing
	}

	private void showMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		Intent originalIntent = getIntent();
		if (originalIntent != null) {
			intent.setAction(originalIntent.getAction());
			intent.putExtras(originalIntent);
		}
		startActivity(intent);
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		Log.e("hwang-tvremote", "StartupActivity : onDestroy()");
	}

	// No used
	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		if (keystoreAvailable) {
			Log.i("hwang-tvremote", "StartupActivity : onNewIntent()");
			showMainActivity();
		}
	}

	private class KeystoreInitializerTask extends AsyncTask<KeyStoreManager, Void, Void> {
		private final String id;

		public KeystoreInitializerTask(String id) {
			this.id = id;
		}

		@Override
		protected Void doInBackground(KeyStoreManager... keyStoreManagers) {
			if (keyStoreManagers.length != 1) {
				throw new IllegalStateException("Only one key store manager expected");
			}
			keyStoreManagers[0].initializeKeyStore(id);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			long elapsedTime = watch.time();
			//long elapsedTime = watch.time(TimeUnit.SECONDS);
			Log.e("hwang-tvremote", "StartupActivity : KeystoreInitializerTask elapsedTime : " + elapsedTime); // 1266, 1455

			keystoreAvailable = true;
			showMainActivity();
		}
	}

	private String getUniqueId() {
		String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		// null ANDROID_ID is possible on emulator
		return id != null ? id : "emulator";
	}
}
