package com.gboomba;

import android.app.Application;

import com.networking.NetworkEngine;

public class GboombaEx extends Application {
	public static boolean ENABLE_LOG = true;

	@Override
	public void onCreate() {
		super.onCreate();
		NetworkEngine.getInstance().init(this);
		NetworkEngine.getInstance().setUseCache(true);
	}
}
