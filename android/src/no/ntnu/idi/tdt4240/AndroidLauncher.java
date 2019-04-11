package no.ntnu.idi.tdt4240;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import no.ntnu.idi.tdt4240.RiskyRisk;

//IMPORT COM.GOOGLE modules here

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RiskyRisk game = new RiskyRisk();
		initialize(game, config);
	}
}
