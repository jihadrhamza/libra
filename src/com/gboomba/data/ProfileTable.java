package com.gboomba.data;

import android.database.sqlite.SQLiteDatabase;

public class ProfileTable {

	public static final String TABLE_PROFILE = "profile";
	public static final String COLUMN_EMAIL_ADDRESS = "EmailAddress";
	public static final String COLUMN_MOBILE_NUMBER = "MobileNumber";
	public static final String COLUMN_USERID = "UserId";

	public static String TAG = AppSqliteHelper.class.getName();

	public static final String CREATE_TABLE_PROFILE = "create table " + TABLE_PROFILE + " " + "(auto_id integer primary key autoincrement,"
			+ COLUMN_EMAIL_ADDRESS + " text not null, " + COLUMN_MOBILE_NUMBER + " text not null, " + COLUMN_USERID + " text null);";

	public static void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(CREATE_TABLE_PROFILE);
	}

	public static void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		onCreate(sqLiteDatabase);
	}
}
