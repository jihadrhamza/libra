package com.gboomba.activities;

import com.gboomba.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ForgotPasswordActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.enter, R.anim.exit);
		
		setContentView(R.layout.activity_forgotpassword);
	}
	
	protected void onPause() {
		super.onPause();
		this.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);

	}
}
