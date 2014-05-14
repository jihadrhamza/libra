package com.gboomba.fragments;

import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignUpOperation;
import com.gboomba.operations.SignUpOperation.SignUpOperationListener;

public class SignInFragment extends Fragment implements SignUpOperationListener {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signin, container, false);
		//triggerSignUpService();
		return view;
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

}
