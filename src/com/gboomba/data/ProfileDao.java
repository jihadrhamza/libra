package com.gboomba.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.gboomba.entities.Profile;

public class ProfileDao {

	public void insertProfile(ContentValues values) {
		AppSqliteHelper.getInstance().appSqLiteDatabase.delete(ProfileTable.TABLE_PROFILE, null, null);
		AppSqliteHelper.getInstance().appSqLiteDatabase.insert(ProfileTable.TABLE_PROFILE, null, values);
	}

	public Profile fetchProfile() {
		Cursor cursor = AppSqliteHelper.getInstance().appSqLiteDatabase.query(ProfileTable.TABLE_PROFILE, null, null,
				null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			return new Profile().getProfilePojo(cursor);
		} else {
			return null;
		}
	}
}
