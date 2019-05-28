package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class SettingsModel {
    public static final SettingsModel INSTANCE = new SettingsModel();
    public static final int MIN_NUM_PLAYERS = 2;
    public static final int MAX_NUM_PLAYERS = 8;

    private static final String FILENAME = "GAME_SETTINGS";

    private Preferences prefs;

    private int numPlayers;

    private SettingsModel() {}

    public static void init() {
        INSTANCE._init();
    }

    private void _init() {
        prefs = Gdx.app.getPreferences(FILENAME);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public static void setNumPlayers(int numPlayers) {
        if (numPlayers != MathUtils.clamp(numPlayers, MIN_NUM_PLAYERS, MAX_NUM_PLAYERS))
            throw new IllegalArgumentException("The number of players has to be within the range "
                                               + "[" + MIN_NUM_PLAYERS + ", " + MAX_NUM_PLAYERS + "]!");

        INSTANCE.numPlayers = numPlayers;
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
