package com.gboomba.fragments;

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
import android.widget.Toast;

import com.gboomba.R;
import com.gboomba.activities.SignInActivity;
import com.gboomba.operations.Operations;
import com.gboomba.operations.ResetPasswordOperation;
import com.gboomba.operations.ResetPasswordOperation.ResetPasswordOperationListener;

public class ResetPasswordFragment extends Fragment implements
		ResetPasswordOperationListener {

	EditText mobileEdtTxt, otpEdtTxt, newPasswordEdtTxt;

	ImageView new_password_row_border, otp_row_border, mobile_row_border;
	ProgressDialog mProgressDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_resetpassword,
				container, false);
		Button resetPasswordButton = (Button) view
				.findViewById(R.id.resetpassword_button);
		mobileEdtTxt = (EditText) view.findViewById(R.id.mobile_field);
		otpEdtTxt = (EditText) view.findViewById(R.id.otp_field);
		ImageView backNavigation = (ImageView) view
				.findViewById(R.id.backNavigation);

		new_password_row_border = (ImageView) view
				.findViewById(R.id.new_password_row_border);
		otp_row_border = (ImageView) view.findViewById(R.id.otp_row_border);
		mobile_row_border = (ImageView) view
				.findViewById(R.id.mobile_row_border);

		backNavigation.setOnClickListener(mOnClickListener);
		newPasswordEdtTxt = (EditText) view
				.findViewById(R.id.newpassword_field);
		resetPasswordButton.setOnClickListener(mOnClickListener);
		mobileEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		otpEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		mobileEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity()
				.getString(R.string.please_wait));

		return view;
	}

	View.OnClickListener mOnClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.resetpassword_button:
				hideKeyboard();
				String mobileNumber = mobileEdtTxt.getText().toString();
				String otpNumber = otpEdtTxt.getText().toString();
				String newPassword = newPasswordEdtTxt.getText().toString();
				if (mobileNumber.isEmpty() || otpNumber.isEmpty()
						|| newPassword.isEmpty()) {
					Toast.makeText(getActivity(),
							getText(R.string.mobile_blank), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (!isNumber(mobileNumber)) {
					showAlertDialog(getText(R.string.invalid_mobile_number)
							.toString());
					return;
				}
				trgrResetPasswordOperation(mobileNumber, otpNumber, newPassword);
				break;
			case R.id.backNavigation:
				getActivity().finish();
				break;
			}
		}
	};

	View.OnFocusChangeListener mFocusChangeListener = new View.OnFocusChangeListener() {

		@Override
		public void onFocusChange(View arg0, boolean arg1) {
			switch (arg0.getId()) {
			case R.id.mobile_field:
				if (arg0.hasFocus()) {
					mobile_row_border
							.setImageResource(R.drawable.border_form_elements_selected);
				} else {
					mobile_row_border
							.setImageResource(R.drawable.border_form_elements);
				}
				break;
			case R.id.otp_field:
				if (arg0.hasFocus()) {
					otp_row_border
							.setImageResource(R.drawable.border_form_elements_selected);
				} else {
					otp_row_border
							.setImageResource(R.drawable.border_form_elements);
				}
				break;
			case R.id.newpassword_field:
				if (arg0.hasFocus()) {
					new_password_row_border
							.setImageResource(R.drawable.border_form_elements_selected);
				} else {
					new_password_row_border
							.setImageResource(R.drawable.border_form_elements);
				}
				break;
			}

		}
	};

	private void trgrResetPasswordOperation(String mobileNumber, String otp,
			String newPassword) {
		if (mProgressDialog != null && !mProgressDialog.isShowing())
			mProgressDialog.show();
		ResetPasswordOperation resetPasswordOperation = new ResetPasswordOperation(
				mobileNumber, otp, newPassword,
				Operations.RESET_PASSWORD_SERVICE_ENDPOINT);
		resetPasswordOperation.doSResetPassword(this);

	}

	private void switchToSignInScreen() {
		Intent intent = new Intent(getActivity(), SignInActivity.class);
		// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		getActivity().startActivity(intent);
		getActivity().finish();
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null)
			inputManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void onResetPasswordServiceFinished(String message) {
		// TODO Auto-generated method stub
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		Toast.makeText(getActivity(), message, 1000).show();
		switchToSignInScreen();
		getActivity().finish();
	}

	@Override
	public void onResetPasswordServiceFailed(String error) {
		if (mProgressDialog != null && mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		Toast.makeText(getActivity(), error, 1000).show();
	}

	private boolean isNumber(String number) {

		try {
			long val = Long.valueOf(number);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Invalid");
			return false;
		}
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
