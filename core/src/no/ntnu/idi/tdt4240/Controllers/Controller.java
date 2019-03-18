package no.ntnu.idi.tdt4240.Controllers;

import no.ntnu.idi.tdt4240.Models.SettingsModel;
import no.ntnu.idi.tdt4240.Views.SettingsView;

public class Controller {
    private final SettingsModel model;
    private final SettingsView view;

    public Controller(SettingsView view) {
        this.model = new SettingsModel();
        this.view = view;
    }

    // In charge of updating view when changes are made
    public void render() {
    }

    public void update() {
        view.hide();
        view.show();
    }

    public void setPreference(String key, Object value) {
        if (value instanceof String) {
            model.setString(key, (String)value);
        } else if (value instanceof Integer) {
            model.setInteger(key, (Integer) value);
        } else if (value instanceof Boolean) {
            model.setBoolean(key, (Boolean)value);
        } else {
            System.out.println("preference type invalid");
        }
    }

    public String getSetting1() {
        return model.getString("setting1", "default value");
    }

    public int getSetting2() {
        return model.getInteger("setting2", 1);
    }

    public boolean getSetting3() {
        return model.getBoolean("setting3", true);
    }

    public void setSetting1(String string) {
        model.setString("setting1", string);
        this.update();
    }

    public void setSetting2(int integer) {
        model.setInteger("setting2", integer);
    }

    public void setSetting3(boolean bool) {
        model.setBoolean("setting3", bool);
    }

}