package no.ntnu.idi.tdt4240;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import no.ntnu.idi.tdt4240.client.GPGSClient;

public class AndroidLauncher extends AndroidApplication {
    private static final String TAG = "OnActivity";

    private GPGSClient gpgsClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // Keep the screen on
        config.useWakelock = true;

        //For popups
        View view = findViewById(android.R.id.content);

        gpgsClient = new GPGSClient(this, view);
        initialize(new RiskyRisk(gpgsClient), config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (gpgsClient != null)
            gpgsClient.onActivityResult(requestCode, resultCode, data);
    }
}
