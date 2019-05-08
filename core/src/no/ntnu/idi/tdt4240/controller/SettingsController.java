package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsController {
    private final SettingsModel model;
    // TODO: Create SettingsView class
//    private final SettingsView view;

    public SettingsController(SettingsModel model) {
//        view = new SettingsView();
        this.model = model;
    }

    public void setSetting1(String string) {
        model.setString("setting1", string);
//        view.setSetting1(getSetting1());
    }

    public String getSetting1() {
        return model.getString("setting1", "null");
    }
}

