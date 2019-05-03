package no.ntnu.idi.tdt4240.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import no.ntnu.idi.tdt4240.RiskyRisk;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1227; // TODO: temporary window size
        config.height = 601;
        new LwjglApplication(new RiskyRisk(), config);
    }
}
