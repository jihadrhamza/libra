package com.gboomba.fragments;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignUpOperation;
import com.gboomba.operations.SignUpOperation.SignUpOperationListener;


public class SignInFragment extends Fragment implements SignUpOperationListener {

	private LoginListener mLoginListener;
	public interface LoginListener {
		public void onFacbookLoginClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signin, container, false);
		// triggerSignUpService();
		LinearLayout fbLoginLayout = (LinearLayout) view.findViewById(R.id.facebook_layout);
		fbLoginLayout.setOnClickListener(mOnClickListener);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mLoginListener = (LoginListener) activity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void triggerSignUpService() {
		Random mRandom = new Random();
		mRandom.nextInt();
		SignUpOperation signUpOperation = new SignUpOperation("someemail" + mRandom.nextInt() + "@gnail.com", "74066677" + mRandom.nextInt(), "password",
				Operations.SIGNUP_SERVICE_ENDPOINT);
		signUpOperation.doSignUp(this);
	}

	@Override
	public void onSignUpServiceFinished() {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "Signup Succeed! Email Address Registered", 1000).show();
	}

	@Override
	public void onSignUpServiceFailed(String error) {
		// TODO Auto-generated method stub

	}
	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.facebook_layout:
				mLoginListener.onFacbookLoginClicked();
				break;
			}
			
		}
	};
	

}
