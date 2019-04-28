package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import no.ntnu.idi.tdt4240.Controllers.SettingsViewer;

public class SettingsModel {

    Preferences prefs;
    private final String FILENAME = "GAME_SETTINGS";

    public SettingsModel() {
        this.prefs = Gdx.app.getPreferences(FILENAME);
    }

    public void setString(String key, String value) {
        prefs.putString(key, value);
        prefs.flush();
    }

    public void setInteger(String key, int value) {
        prefs.putInteger(key, value);
        prefs.flush();
    }

    public void setBoolean(String key, boolean value) {
        prefs.putBoolean(key, value);
        prefs.flush();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public int getInteger(String key, Integer defaultValue) {
        return prefs.getInteger(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }



}
