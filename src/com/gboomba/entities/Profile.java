package com.gboomba.entities;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.database.Cursor;

import com.gboomba.data.ProfileDao;
import com.gboomba.data.ProfileTable;

public class Profile {

	public String emailAddress;
	public String mobileNumber;
	public String userId;

	public Profile getProfilePojo(Cursor cursor) {

		try {
			cursor.moveToFirst();
			emailAddress = cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_EMAIL_ADDRESS));
			mobileNumber = cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_MOBILE_NUMBER));
			userId = cursor.getString(cursor.getColumnIndex(ProfileTable.COLUMN_USERID));
			return this;

		} catch (Exception e) {

		} finally {
			if (cursor != null && !cursor.isClosed())
				cursor.close();
		}
		return null;
	}

	public void parse(JSONObject userProfile) throws JSONException {
		ContentValues values = new ContentValues();
		values.put(ProfileTable.COLUMN_EMAIL_ADDRESS, userProfile.getString(ProfileTable.COLUMN_EMAIL_ADDRESS));
		values.put(ProfileTable.COLUMN_MOBILE_NUMBER, userProfile.getString(ProfileTable.COLUMN_MOBILE_NUMBER));
		values.put(ProfileTable.COLUMN_USERID, userProfile.getString(ProfileTable.COLUMN_USERID));
		new ProfileDao().insertProfile(values);
	}

}
