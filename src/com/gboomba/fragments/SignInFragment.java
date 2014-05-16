package com.gboomba.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.activities.ForgotPasswordActivity;
import com.gboomba.activities.SignUpActivity;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignInOperation;
import com.gboomba.operations.SignInOperation.SignInOperationListener;

public class SignInFragment extends Fragment implements SignInOperationListener {

	private LoginListener mLoginListener;
	private EditText emailEdttxt, passwordEdtTxt;
	private ProgressDialog mProgressDialog;

	public interface LoginListener {
		public void onFacbookLoginClicked();

		public void onGoogleLoginClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signin, container, false);
		// triggerSignUpService();
		LinearLayout fbLoginLayout = (LinearLayout) view.findViewById(R.id.facebook_layout);
		LinearLayout googleLoginLayout = (LinearLayout) view.findViewById(R.id.google_layout);
		TextView signUpTextView = (TextView) view.findViewById(R.id.signup_txt);
		Button signInButton = (Button) view.findViewById(R.id.signInButton);
		emailEdttxt = (EditText) view.findViewById(R.id.email_field);
		passwordEdtTxt = (EditText) view.findViewById(R.id.password_field);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getString(R.string.SignIn));
		ImageView forgotPasswordImgView = (ImageView) view.findViewById(R.id.forgotPasswordIcon);
		forgotPasswordImgView.setOnClickListener(mOnClickListener);
		googleLoginLayout.setOnClickListener(mOnClickListener);
		fbLoginLayout.setOnClickListener(mOnClickListener);
		signUpTextView.setOnClickListener(mOnClickListener);
		signInButton.setOnClickListener(mOnClickListener);
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

	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.facebook_layout:
				mLoginListener.onFacbookLoginClicked();
				break;
			case R.id.google_layout:
				mLoginListener.onGoogleLoginClicked();
				break;
			case R.id.signup_txt:
				switchToSignupScreen();
				break;
			case R.id.signInButton:
				hideKeyboard();
				String email = emailEdttxt.getText().toString();
				String password = passwordEdtTxt.getText().toString();
				if (email != null && email.isEmpty()) {
					Toast.makeText(getActivity(), getText(R.string.email_blank), Toast.LENGTH_SHORT).show();
					return;
				} else if (password != null && password.isEmpty()) {
					Toast.makeText(getActivity(), getText(R.string.password_blank), Toast.LENGTH_SHORT).show();
					return;
				}
				trgrSignInOperation(email, password);
				break;
				
			case R.id.forgotPasswordIcon:
				switchToForgotPasswordScreen();
				break;
			}

		}
	};

	private void switchToSignupScreen() {
		Intent intent = new Intent(getActivity(), SignUpActivity.class);
		startActivity(intent);
	}
	
	private void switchToForgotPasswordScreen() {
		Intent intent = new Intent(getActivity(), ForgotPasswordActivity.class);
		startActivity(intent);
	}

	@Override
	public void onSignInServiceFinished() {
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
	}

	@Override
	public void onSignInServiceFailed(String error) {
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
		Toast.makeText(getActivity(), error, 1000).show();

	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null)
			inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void trgrSignInOperation(String emailAddress, String password) {
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
		SignInOperation signInOperation = new SignInOperation(emailAddress, password, Operations.SIGNIN_SERVICE_ENDPOINT);
		signInOperation.doSignIn(this);
	}

}
