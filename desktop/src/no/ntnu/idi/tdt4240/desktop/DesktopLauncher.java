package no.ntnu.idi.tdt4240.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.desktop.controller.DesktopMockGPGSClient;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        // Size of `risk_game_map.png`, for simplicity:
        config.width = 1227;
        config.height = 601;
        new LwjglApplication(new RiskyRisk(new DesktopMockGPGSClient()), config);
    }
}
