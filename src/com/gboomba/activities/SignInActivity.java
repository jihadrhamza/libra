package com.gboomba.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.gboomba.R;
import com.gboomba.fragments.SignInFragment;
import com.gboomba.fragments.SignInFragment.LoginListener;
import com.gboomba.social.FacebookActions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

public class SignInActivity extends FragmentActivity implements LoginListener, ConnectionCallbacks, OnConnectionFailedListener {
	private final String PENDING_ACTION_BUNDLE_KEY = "com.gboomba:PendingAction";
	private UiLifecycleHelper mUIHelper;
	private LoginButton mFBLoginButton;
	private PendingAction pendingAction = PendingAction.NONE;
	private final String TAG = this.getClass().getName();
	private GraphUser mFBGraphUser;
	private ArrayList<String> fbPermissions;
	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress = false;
	private static final int RC_SIGN_IN = 0;
	private ConnectionResult mConnectionResult;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.enter, R.anim.exit);
		FacebookActions.printSSHKey(this);
		setContentView(R.layout.activity_signin);
		mUIHelper = new UiLifecycleHelper(this, callback);
		mUIHelper.onCreate(savedInstanceState);
		initFacebook();
		initGoogle();

	}

	private void initFacebook() {
		mFBLoginButton = (LoginButton) findViewById(R.id.login_button);
		fbPermissions = new ArrayList<String>();
		fbPermissions.add("basic_info");
		fbPermissions.add("email");
		mFBLoginButton.setReadPermissions(fbPermissions);
		mFBLoginButton.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
		mFBLoginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				Log.d(TAG, "onUserInfoFetched");
				mFBGraphUser = user;
				if (mFBGraphUser != null) {
					Log.d(TAG, "mFBGraphUser != null");
					// performSignInFromFacebook();
				} else {
					Log.d(TAG, "User logged out");
				}
			}
		});
	}

	private void initGoogle() {
		mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, null)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	/**
	 * FB Related Cycle Handling
	 */

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
		AppEventsLogger.activateApp(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mUIHelper.onSaveInstanceState(outState);
		outState.putString(PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
	}

	@Override
	public void onPause() {
		super.onPause();
		this.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
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

	@Override
	public void onGoogleLoginClicked() {
		Log.d(TAG, "onGoogleLoginClicked");
		signInWithGplus();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
			Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			String personName = currentPerson.getDisplayName();
			String personPhotoUrl = currentPerson.getImage().getUrl();
			String personGooglePlusProfile = currentPerson.getUrl();
			String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
			Log.e(TAG, "Name: " + personName + ", plusProfile: " + personGooglePlusProfile + ", email: " + email + ", Image: " + personPhotoUrl);

		} else {
			Toast.makeText(getApplicationContext(), "Person information is null", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {

		Toast.makeText(this, "User is not connected!", Toast.LENGTH_LONG).show();
		if (!result.hasResolution()) {
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}
		if (!mIntentInProgress) {
			mConnectionResult = result;
			resolveSignInError();
		}
	}

	@Override
	public void onConnectionSuspended(int cause) {
	}

	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mGoogleApiClient.connect();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			if (resultCode != RESULT_OK) {

			}
			mIntentInProgress = false;
			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		} else {

			mUIHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
			FacebookActions facebookActions = FacebookActions.getInstance();
			Session session = facebookActions.getSession(this);
			Log.d(TAG, "FB Access Token " + session.getAccessToken());
		}
	}


}
