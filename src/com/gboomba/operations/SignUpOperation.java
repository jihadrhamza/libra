package com.gboomba.operations;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gboomba.Utils;
import com.gboomba.entities.Profile;
import com.networking.NetworkEngine;
import com.networking.NetworkEngine.HttpMethod;
import com.networking.NetworkOperation;

public class SignUpOperation extends NetworkOperation {

	private static String TAG = SignInOperation.class.getName();

	public interface SignUpOperationListener {
		void onSignUpServiceFinished();

		void onSignUpServiceFailed(String error);
	}

	public SignUpOperation(String userEmail, String mobileNumber, String password, String url) {
		super(url, null, HttpMethod.POST);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		addHeaders(headers);
		HashMap<String, String> params = new HashMap<String, String>();
		setJSONStringForPost(true);
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("EmailAddress", userEmail);
			jsonObject.put("MobileNumber", mobileNumber);
			jsonObject.put("Password", password);

		} catch (JSONException e) {
			return;

		}
		params.put("", jsonObject.toString());
		Utils.displayLogs(TAG, jsonObject.toString());
		addParams(params);

	}

	public void doSignUp(final SignUpOperationListener listener) {
		setListener(new OperationListener() {

			@Override
			public void onError(NetworkOperation operation) {
				//Log.d("", operation.getResponseString());
			}

			@Override
			public void onCompletion(NetworkOperation operation) {
				try {
					Utils.displayLogs(TAG, "" + operation.getResponseString());
					JSONObject signUpResponse = new JSONObject(operation.getResponseString());
					JSONObject responseHeader = signUpResponse.optJSONObject("responseHeader");
					if (responseHeader.getString("Code").compareTo("0") == 0) {
						new Profile().parse(signUpResponse.optJSONObject("userProfile"));
						listener.onSignUpServiceFinished();

					} else {
						listener.onSignUpServiceFailed(responseHeader.getString("Message"));
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
					listener.onSignUpServiceFailed("");
				} catch (Exception e) {
					listener.onSignUpServiceFailed("");
					e.printStackTrace();
				}
			}
		});

		NetworkEngine.getInstance().enqueueOperation(this);
	}

}
