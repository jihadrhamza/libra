package com.gboomba.activities;

import com.gboomba.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_layout);
		swapToSignInActivity();
	}

	/**
	 * Load the SignIn fragment
	 * 
	 */

	private void swapToSignInActivity() {

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
				startActivity(intent);
				finish();

			}

		}, 3000);

	}
}
