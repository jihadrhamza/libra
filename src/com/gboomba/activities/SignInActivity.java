package com.gboomba.activities;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.gboomba.R;
import com.gboomba.fragments.SignInFragment.LoginListener;
import com.gboomba.social.FacebookActions;

public class SignInActivity extends FragmentActivity implements LoginListener{
	private final String PENDING_ACTION_BUNDLE_KEY = "com.gboomba:PendingAction";
	private UiLifecycleHelper mUIHelper;
	private LoginButton mFBLoginButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private final String TAG 	=	this.getClass().getName();
	private GraphUser mFBGraphUser;
	private ArrayList<String> fbPermissions;
	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FacebookActions.printSSHKey(this);
		setContentView(R.layout.activity_signin);
		mUIHelper = new UiLifecycleHelper(this, callback);
		mUIHelper.onCreate(savedInstanceState);
		initFacebook();
	}
	

	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			// onSessionStateChange(session, state, exception);
		}
	};

	private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
			Log.d(TAG, String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
			Log.d(TAG, "Success!");
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		mUIHelper.onResume();
		// Call the 'activateApp' method to log an app event for use in
		// analytics and advertising reporting. Do so in the onResume methods
		// of the primary Activities that an app may be launched into.
		AppEventsLogger.activateApp(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mUIHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mUIHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
		FacebookActions facebookActions = FacebookActions.getInstance();
		Session session = facebookActions.getSession(this);
		Log.d(TAG, "FB Access Token " + session.getAccessToken());
	}

	@Override
	public void onPause() {
		super.onPause();
		mUIHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mFBGraphUser != null)
			mFBLoginButton.performClick(); // Logout
		mUIHelper.onDestroy();
	}

	@Override
	public void onFacbookLoginClicked() {
		Log.d(TAG, "onFacbookLoginClicked");
		mFBLoginButton.performClick();
	}
	
	private void initFacebook() {
		mFBLoginButton = (LoginButton) findViewById(R.id.login_button);
		fbPermissions = new ArrayList<String>();
		// fbPermissions.add("publish_stream");
		// fbPermissions.add("user_likes");
		fbPermissions.add("basic_info");
		fbPermissions.add("email");
		// fbPermissions.add("user_birthday");
		mFBLoginButton.setReadPermissions(fbPermissions);
		mFBLoginButton.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);

		mFBLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				Log.d(TAG, "onUserInfoFetched");
				mFBGraphUser = user;
				if (mFBGraphUser != null) {
					Log.d(TAG, "mFBGraphUser != null");
					//performSignInFromFacebook();
				} else {
					Log.d(TAG, "User logged out");
				}
			}
		});
	}

}
