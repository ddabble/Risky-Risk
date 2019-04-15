package no.ntnu.idi.tdt4240;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	private static final String TAG = "OnActivity";
	private GPGSClient gpgsClient;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RiskyRisk game = new RiskyRisk();
		gpgsClient = new GPGSClient(this);
		game.gpgsClient = gpgsClient;
		initialize(game, config);
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (gpgsClient != null)
            gpgsClient.onActivityResult(requestCode, resultCode, data);
    }
}
