package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsController {
    // TODO: Create SettingsView class
//    private final SettingsView view;

    public SettingsController() {
//        view = new SettingsView();
    }

    public void init() {
        SettingsModel.INSTANCE.init();
    }

    public void setSetting1(String string) {
        SettingsModel.INSTANCE.setString("setting1", string);
//        view.setSetting1(getSetting1());
    }

    public String getSetting1() {
        return SettingsModel.INSTANCE.getString("setting1", "null");
    }
}

