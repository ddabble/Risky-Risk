package no.ntnu.idi.tdt4240.Models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsModel {

    Preferences prefs;
    private final String FILENAME = "GAME_SETTINGS";

    public SettingsModel() {
        this.prefs = Gdx.app.getPreferences(FILENAME);
    }

    public void setPref(String pref, String value) {
        prefs.putString(pref, value);
        prefs.flush();
    }

    public void setPref(String pref, int value) {
        prefs.putInteger(pref, value);
        prefs.flush();
    }

    public void setPref(String pref, boolean value) {
        prefs.putBoolean(pref, value);
        prefs.flush();
    }

    public void getPref(String pref) {

    }

}