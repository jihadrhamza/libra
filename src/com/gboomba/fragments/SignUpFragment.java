package com.gboomba.fragments;

import org.apache.commons.validator.EmailValidator;

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
import android.widget.TextView;
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.activities.HomeActivity;
import com.gboomba.activities.WebviewActivity;
import com.gboomba.operations.Operations;
import com.gboomba.operations.SignUpOperation;
import com.gboomba.operations.SignUpOperation.SignUpOperationListener;

public class SignUpFragment extends Fragment implements SignUpOperationListener {

	EditText emailEdtTxt, mobileEdtTxt, passwordEdtTxt;
	Button signupButton;
	private ProgressDialog mProgressDialog;
	ImageView email_row_border,password_row_border,mobile_row_border;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_signup, container, false);
		TextView signInTextView = (TextView) view.findViewById(R.id.signin_txt);
		emailEdtTxt = (EditText) view.findViewById(R.id.email_field);
		mobileEdtTxt = (EditText) view.findViewById(R.id.mobile_field);
		passwordEdtTxt = (EditText) view.findViewById(R.id.password_field);
		signupButton = (Button) view.findViewById(R.id.signUpButton);
		email_row_border	=	(ImageView) view.findViewById(R.id.email_row_border);
		password_row_border	=	(ImageView) view.findViewById(R.id.password_row_border);
		mobile_row_border	=	(ImageView) view.findViewById(R.id.mobile_row_border);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getString(R.string.SignUp));
		signInTextView.setOnClickListener(mOnClickListener);
		signupButton.setOnClickListener(mOnClickListener);
		
		emailEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		passwordEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		mobileEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		
		((TextView) view.findViewById(R.id.termsofuse)).setOnClickListener(mOnClickListener);
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
			case R.id.termsofuse:
				switchToWebviewcreen();
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
				
				if (!isNumber(mobile)) {
					showAlertDialog(getText(R.string.invalid_mobile_number).toString());
					return;
				}

				triggerSignUpService(email, mobile, password);
				break;

			}

		}
	};
	
	View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			switch (arg0.getId()) {
			case R.id.email_field:
				if(arg0.hasFocus()){
					email_row_border.setImageResource(R.drawable.border_form_elements_selected);
				}else{
					email_row_border.setImageResource(R.drawable.border_form_elements);
				}
				break;
			case R.id.password_field:
				if(arg0.hasFocus()){
					password_row_border.setImageResource(R.drawable.border_form_elements_selected);
				}else{
					password_row_border.setImageResource(R.drawable.border_form_elements);
				}
				break;
				
			case R.id.mobile_field:
				if(arg0.hasFocus()){
					mobile_row_border.setImageResource(R.drawable.border_form_elements_selected);
				}else{
					mobile_row_border.setImageResource(R.drawable.border_form_elements);
				}
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

	private void switchToHomecreen() {
		Intent intent = new Intent(getActivity(), HomeActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
	
	private void switchToWebviewcreen() {
		Intent intent = new Intent(getActivity(), WebviewActivity.class);
		startActivity(intent);
	
	}
	@Override
	public void onSignUpServiceFinished() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		
		switchToHomecreen();
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
