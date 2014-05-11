package com.gboomba.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gboomba.Utils;

public class AppSqliteHelper extends SQLiteOpenHelper {

	public static String TAG = AppSqliteHelper.class.getName();
	private static final String DATABASE_NAME = "gboomba.db";
	private static final int DATABASE_VERSION = 1;
	private static AppSqliteHelper sqLiteOpenHelper;
	public SQLiteDatabase appSqLiteDatabase;

	public static AppSqliteHelper getInstance(Context context) {
		if (sqLiteOpenHelper == null) {
			sqLiteOpenHelper = new AppSqliteHelper(context);
		}
		return sqLiteOpenHelper;
	}

	public static AppSqliteHelper getInstance() {
		if (sqLiteOpenHelper != null) {
			return sqLiteOpenHelper;
		} else
			return null;
	}

	private AppSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteOpenHelper = this;
		try {
			if (appSqLiteDatabase == null)
				appSqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Utils.displayLogs(TAG, "Creating Tables....");
		ProfileTable.onCreate(database);
		Utils.displayLogs(TAG, "Creating Tables Done ....");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		ProfileTable.onUpgrade(db, oldVersion, newVersion);

	}
}
