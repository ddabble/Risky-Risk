package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SettingsModel {
    public static final SettingsModel INSTANCE = new SettingsModel();
    private static final String FILENAME = "GAME_SETTINGS";

    private Preferences prefs;

    private int numberOfPlayers;

    private SettingsModel() {}

    public void init() {
        prefs = Gdx.app.getPreferences(FILENAME);
    }

    public int getNumPlayers() {
        return numberOfPlayers;
    }

    public void setNumPlayers(int num) {
        if (num > 6)
            numberOfPlayers = 6;
        else if (num < 2)
            numberOfPlayers = 2;
        else
            numberOfPlayers = num;
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
