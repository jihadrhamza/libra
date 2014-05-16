package com.gboomba.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.activities.ResetPasswordActivity;
import com.gboomba.activities.SignUpActivity;
import com.gboomba.operations.GetOtpOperation;
import com.gboomba.operations.GetOtpOperation.GetOtpInOperationListener;
import com.gboomba.operations.Operations;

public class ForgotPasswordFragment extends Fragment implements GetOtpInOperationListener {

	EditText mobileEdtTxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
		Button getoptButton = (Button) view.findViewById(R.id.getopt_button);
		mobileEdtTxt = (EditText) view.findViewById(R.id.mobile_field);
		getoptButton.setOnClickListener(mOnClickListener);
		return view;
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.getopt_button:
				hideKeyboard();
				String mobileBumber = mobileEdtTxt.getText().toString();
				if (mobileBumber.isEmpty()) {
					Toast.makeText(getActivity(), getText(R.string.mobile_blank), Toast.LENGTH_SHORT).show();
					return;
				}

				trgrGetOptOperation(mobileBumber);
				break;
			}
		}
	};

	private void trgrGetOptOperation(String mobileNumber) {
		GetOtpOperation getOtpOperation = new GetOtpOperation(mobileNumber, Operations.GETOTP_SERVICE_ENDPOINT);
		getOtpOperation.getOTP(this);

	}

	@Override
	public void onGetOtpServiceFinished(String message) {
		Toast.makeText(getActivity(), message, 1000).show();
		switchToResetPasswordScreen();
	}

	@Override
	public void onGetOtpServiceFailed(String error) {
		Toast.makeText(getActivity(), error, 1000).show();
	}

	private void switchToResetPasswordScreen() {
		Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
		startActivity(intent);
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null)
			inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
