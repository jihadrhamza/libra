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
import com.gboomba.activities.ResetPasswordActivity;
import com.gboomba.activities.SignUpActivity;
import com.gboomba.operations.GetOtpOperation;
import com.gboomba.operations.GetOtpOperation.GetOtpInOperationListener;
import com.gboomba.operations.Operations;

public class ForgotPasswordFragment extends Fragment implements
		GetOtpInOperationListener {

	private ProgressDialog mProgressDialog;
	EditText mobileEdtTxt;
	ImageView mobile_row_border;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_forgot_password,
				container, false);
		Button getoptButton = (Button) view.findViewById(R.id.getopt_button);
		mobileEdtTxt = (EditText) view.findViewById(R.id.mobile_field);
		mobile_row_border = (ImageView) view
				.findViewById(R.id.mobile_row_border);
		getoptButton.setOnClickListener(mOnClickListener);
		ImageView backNagivationView = (ImageView) view
				.findViewById(R.id.backNavigation);
		backNagivationView.setOnClickListener(mOnClickListener);
		mobileEdtTxt.setOnFocusChangeListener(mFocusChangeListener);
		mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getString(R.string.please_wait));
	
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
					Toast.makeText(getActivity(),
							getText(R.string.mobile_blank), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				

				if (!isNumber(mobileBumber)) {
					showAlertDialog(getText(R.string.invalid_mobile_number).toString());
					return;
				}


				trgrGetOptOperation(mobileBumber);
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

	private void trgrGetOptOperation(String mobileNumber) {
		if(mProgressDialog!=null&&!mProgressDialog.isShowing())
			mProgressDialog.show();
		GetOtpOperation getOtpOperation = new GetOtpOperation(mobileNumber,
				Operations.GETOTP_SERVICE_ENDPOINT);
		getOtpOperation.getOTP(this);

	}

	@Override
	public void onGetOtpServiceFinished(String message) {
		if(mProgressDialog!=null&&mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		Toast.makeText(getActivity(), message, 1000).show();
		switchToResetPasswordScreen();
	}

	@Override
	public void onGetOtpServiceFailed(String error) {
		if(mProgressDialog!=null&&mProgressDialog.isShowing())
			mProgressDialog.dismiss();
		Toast.makeText(getActivity(), error, 1000).show();
	}

	private void switchToResetPasswordScreen() {
		Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
		startActivity(intent);
	}

	private void hideKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputManager != null)
			inputManager.hideSoftInputFromWindow(getActivity()
					.getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
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
	
}
