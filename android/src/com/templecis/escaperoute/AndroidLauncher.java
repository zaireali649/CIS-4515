package com.templecis.escaperoute;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useGyroscope = true;  //default is false
		config.useAccelerometer = true;
//you may want to switch off sensors that are on by default if they are no longer needed.
		config.useCompass = false;

		initialize(new EscapeRouteMain(), config);
	}
}
