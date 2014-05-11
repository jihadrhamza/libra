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

public class SignInOperation extends NetworkOperation {

	private static String TAG = SignInOperation.class.getName();

	public interface SignInOperationListener {
		void onSignInServiceFinished();

		void onSignInServiceFailed(String error);
	}

	public SignInOperation(String userEmail, String password, String url) {
		super(url, null, HttpMethod.POST);
		HashMap<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		addHeaders(headers);
		HashMap<String, String> params = new HashMap<String, String>();
		setJSONStringForPost(true);
		final JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("Email", userEmail);
			jsonObject.put("Password", password);

		} catch (JSONException e) {
			return;

		}
		params.put("", jsonObject.toString());
		Utils.displayLogs(TAG, jsonObject.toString());
		addParams(params);

	}

	public void doSignIn(final SignInOperationListener listener) {
		setListener(new OperationListener() {

			@Override
			public void onError(NetworkOperation operation) {
				Log.d("", operation.getResponseString());
			}

			@Override
			public void onCompletion(NetworkOperation operation) {
				try {
					Utils.displayLogs(TAG, "" + operation.getResponseString());
					JSONObject signUpResponse = new JSONObject(operation.getResponseString());
					JSONObject responseHeader	=	signUpResponse.optJSONObject("responseHeader");
					if (responseHeader.getString("Code").compareTo("0") == 0) {
						new Profile().parse(signUpResponse.optJSONObject("userProfile"));
					}else{
						listener.onSignInServiceFailed("");
					}
					
					
					listener.onSignInServiceFinished();
				} catch (JSONException e) {
					e.printStackTrace();
					listener.onSignInServiceFailed("");
				} catch (Exception e) {
					listener.onSignInServiceFailed("");
					e.printStackTrace();
				}
			}
		});

		NetworkEngine.getInstance().enqueueOperation(this);
	}

}
