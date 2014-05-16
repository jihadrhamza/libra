package com.gboomba.fragments;

import java.util.Random;

import org.apache.commons.validator.EmailValidator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignUpOperation;
import com.gboomba.operations.SignUpOperation.SignUpOperationListener;

public class SignUpFragment extends Fragment implements SignUpOperationListener {

	EditText emailEdtTxt, mobileEdtTxt, passwordEdtTxt;
	Button signupButton;
	private ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		TextView signInTextView = (TextView) view.findViewById(R.id.signin_txt);
		emailEdtTxt = (EditText) view.findViewById(R.id.email_field);
		mobileEdtTxt = (EditText) view.findViewById(R.id.mobile_field);
		passwordEdtTxt = (EditText) view.findViewById(R.id.password_field);
		signupButton = (Button) view.findViewById(R.id.signUpButton);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getString(R.string.SignUp));
		signInTextView.setOnClickListener(mOnClickListener);
		signupButton.setOnClickListener(mOnClickListener);
		return view;
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.signin_txt:
				getActivity().finish();
				hideKeyboard();
				break;
			case R.id.signUpButton:
				hideKeyboard();
				String email = emailEdtTxt.getText().toString();
				String password = passwordEdtTxt.getText().toString();
				String mobile = mobileEdtTxt.getText().toString();

				if (email.isEmpty() || password.isEmpty() || mobile.isEmpty()) {
					showAlertDialog(getText(R.string.fill_mandatory_details).toString());
					return;
				}
				if ((email != null && !email.isEmpty() && email.charAt(0) == ' ') || (mobile != null && !mobile.isEmpty() && mobile.charAt(0) == ' ')) {
					showAlertDialog(getText(R.string.preceding_white_spaces).toString());
					return;
				}

				if (!EmailValidator.getInstance().isValid(email)) {
					showAlertDialog(getText(R.string.invalid_email_address).toString());
					return;
				}

				triggerSignUpService(email, mobile, password);
				break;

			}

		}
	};

	public void triggerSignUpService(String email, String mobile, String password) {
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
		SignUpOperation signUpOperation = new SignUpOperation(email, mobile, password, Operations.SIGNUP_SERVICE_ENDPOINT);
		signUpOperation.doSignUp(this);
	}

	public void showAlertDialog(String messgae) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
		alertDialogBuilder.setMessage(messgae).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	@Override
	public void onSignUpServiceFinished() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onSignUpServiceFailed(String error) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Toast.makeText(getActivity(), error, 1000).show();
	}
	
	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
