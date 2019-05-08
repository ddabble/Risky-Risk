package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsController {
    private final SettingsModel model;
    // TODO: Create SettingsView class
//    private final SettingsView view;

    public SettingsController() {
//        view = new SettingsView();
        model = new SettingsModel();
    }

    public void init() {
        model.init();
    }

    public void setSetting1(String string) {
        model.setString("setting1", string);
//        view.setSetting1(getSetting1());
    }

    public String getSetting1() {
        return model.getString("setting1", "null");
    }
}

