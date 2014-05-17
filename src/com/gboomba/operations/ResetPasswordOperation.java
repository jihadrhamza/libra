package com.gboomba.operations;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gboomba.Utils;
import com.gboomba.entities.Profile;
import com.gboomba.operations.SignInOperation.SignInOperationListener;
import com.networking.NetworkEngine;
import com.networking.NetworkOperation;
import com.networking.NetworkEngine.HttpMethod;
import com.networking.NetworkOperation.OperationListener;

public class ResetPasswordOperation extends NetworkOperation {

	private static String TAG = ResetPasswordOperation.class.getName();

	public interface ResetPasswordOperationListener {
		void onResetPasswordServiceFinished(String message);

		void onResetPasswordServiceFailed(String error);
	}

	public ResetPasswordOperation(String mobileNumber, String otp,
			String password, String url) {
		super(url, null, HttpMethod.POST);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		addHeaders(headers);
		HashMap<String, String> params = new HashMap<String, String>();
		setJSONStringForPost(true);
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("MobileNumber", mobileNumber);
			jsonObject.put("OTP", otp);
			jsonObject.put("Password", password);
		} catch (JSONException e) {
			return;

		}
		params.put("", jsonObject.toString());
		Utils.displayLogs(TAG, jsonObject.toString());
		addParams(params);

	}

	public void doSResetPassword(final ResetPasswordOperationListener listener) {
		setListener(new OperationListener() {

			@Override
			public void onError(NetworkOperation operation) {
				Log.d("", operation.getResponseString());
			}

			@Override
			public void onCompletion(NetworkOperation operation) {
				try {
					Utils.displayLogs(TAG, "" + operation.getResponseString());
					JSONObject signUpResponse = new JSONObject(
							operation.getResponseString());
					JSONObject responseHeader = signUpResponse
							.optJSONObject("responseHeader");
					if (responseHeader.getString("Code").compareTo("0") == 0) {
						listener.onResetPasswordServiceFinished(responseHeader
								.getString("Message"));

						
					} else if(responseHeader.getString("Code").compareTo("1") == 0){
						listener.onResetPasswordServiceFailed(responseHeader
								.getString("Message"));
					}else{
						
						listener.onResetPasswordServiceFailed(responseHeader
								.getString("Message"));
					}

				} catch (JSONException e) {
					e.printStackTrace();
					listener.onResetPasswordServiceFailed("Check your netwrok settings!");
				} catch (Exception e) {
					listener.onResetPasswordServiceFailed("Check your netwrok settings!");

					e.printStackTrace();
				}
			}
		});

		NetworkEngine.getInstance().enqueueOperation(this);
	}

}
