package com.gboomba.social;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.facebook.Session;
import com.facebook.internal.Utility;

/**
 * Its a singleton class, which provides Facebook Session, which can be accessed
 * throughout the app. Also provides APIs to update post, photos, find friends
 * etc.
 */
public class FacebookActions {

	private static volatile FacebookActions mFacebookActions = null;

	private FacebookActions() {
	};

	public static FacebookActions getInstance() {
		if (mFacebookActions == null) {
			mFacebookActions = new FacebookActions();
		}
		return mFacebookActions;
	}

	public static void printSSHKey(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo("com.gboomba", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get Facebook session object
	 * 
	 * @param context
	 *            context
	 * @return com.facebook.Session, which is used to authenticate a user and
	 *         manage the user's session with Facebook
	 */
	public Session getSession(Context context) {
		if (context == null) {
			return null;
		}
		Session session = Session.getActiveSession();
		if (session != null) {
			return session;
		}
		String applicationId = Utility.getMetadataApplicationId(context);
		if (applicationId == null) {
			return null;
		}
		return Session.openActiveSessionFromCache(context);
	}

	// TODO APIs for update post, photos, find friends etc.
}
