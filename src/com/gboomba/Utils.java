package com.gboomba;

import android.util.Log;

public class Utils {

	public static void displayLogs(String Tag, String message) {
		if (GboombaEx.ENABLE_LOG) {
			Log.e(Tag, message);
		}
	}

}
