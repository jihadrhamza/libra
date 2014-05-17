package com.gboomba.fragments;

import org.apache.commons.validator.EmailValidator;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.gboomba.activities.HomeActivity;
import com.gboomba.activities.SignUpActivity;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignInOperation;
import com.gboomba.operations.SignInOperation.SignInOperationListener;

public class SignInFragment extends Fragment implements SignInOperationListener {

	private LoginListener mLoginListener;
	private EditText emailEdttxt, passwordEdtTxt;
	private ProgressDialog mProgressDialog;

	ImageView email_row_border, password_row_border;

	public interface LoginListener {
		public void onFacbookLoginClicked();

		public void onGoogleLoginClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_signin, container, false);
		// triggerSignUpService();
		LinearLayout fbLoginLayout = (LinearLayout) view
				.findViewById(R.id.facebook_layout);
		LinearLayout googleLoginLayout = (LinearLayout) view
				.findViewById(R.id.google_layout);
		TextView signUpTextView = (TextView) view.findViewById(R.id.signup_txt);
		Button signInButton = (Button) view.findViewById(R.id.signInButton);
		emailEdttxt = (EditText) view.findViewById(R.id.email_field);
		passwordEdtTxt = (EditText) view.findViewById(R.id.password_field);
		email_row_border = (ImageView) view.findViewById(R.id.email_row_border);
		password_row_border = (ImageView) view
				.findViewById(R.id.password_row_border);

		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getString(R.string.SignIn));
		ImageView forgotPasswordImgView = (ImageView) view
				.findViewById(R.id.forgotPasswordIcon);
		forgotPasswordImgView.setOnClickListener(mOnClickListener);
		//googleLoginLayout.setOnClickListener(mOnClickListener);
		//fbLoginLayout.setOnClickListener(mOnClickListener);
		signUpTextView.setOnClickListener(mOnClickListener);
		signInButton.setOnClickListener(mOnClickListener);

		emailEdttxt.setOnFocusChangeListener(mFocusChangeListener);
		passwordEdtTxt.setOnFocusChangeListener(mFocusChangeListener);

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
					Toast.makeText(getActivity(),
							getText(R.string.email_blank), Toast.LENGTH_SHORT)
							.show();
					return;
				}

				if ((email != null && !email.isEmpty() && email.charAt(0) == ' ')) {
					showAlertDialog(getText(R.string.preceding_white_spaces)
							.toString());
					return;
				}

				if (!EmailValidator.getInstance().isValid(email)
						&& !isNumber(email)) {
					showAlertDialog(getText(R.string.invalid_email_phonenumber)
							.toString());
					return;
				}

				if (password != null && password.isEmpty()) {
					Toast.makeText(getActivity(),
							getText(R.string.password_blank),
							Toast.LENGTH_SHORT).show();
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

	private boolean isNumber(String number) {

		try {
			long val = Long.valueOf(number);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Invalid");
			return false;
		}
	}

	View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			switch (arg0.getId()) {
			case R.id.email_field:
				if (arg0.hasFocus()) {
					email_row_border
							.setImageResource(R.drawable.border_form_elements_selected);
				} else {
					email_row_border
							.setImageResource(R.drawable.border_form_elements);
				}
				break;
			case R.id.password_field:
				if (arg0.hasFocus()) {
					password_row_border
							.setImageResource(R.drawable.border_form_elements_selected);
				} else {
					password_row_border
							.setImageResource(R.drawable.border_form_elements);
				}
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

	private void switchToHomecreen() {
		Intent intent = new Intent(getActivity(), HomeActivity.class);
		startActivity(intent);
		getActivity().finish();
	}

	@Override
	public void onSignInServiceFinished() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}

		switchToHomecreen();
	}

	@Override
	public void onSignInServiceFailed(String error) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		Toast.makeText(getActivity(), error, 1000).show();

	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null)
			inputManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void trgrSignInOperation(String emailAddress, String password) {
		if (mProgressDialog != null) {
			mProgressDialog.show();
		}
		SignInOperation signInOperation = new SignInOperation(emailAddress,
				password, Operations.SIGNIN_SERVICE_ENDPOINT);
		signInOperation.doSignIn(this);
	}

	public void showAlertDialog(String messgae) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setMessage(messgae).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

}
