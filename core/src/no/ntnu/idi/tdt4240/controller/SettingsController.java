package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsController {
    private final SettingsModel model;
    private final SettingsViewer viewer;

    public SettingsController(SettingsViewer viewer, SettingsModel model) {
        this.viewer = viewer;
        this.model = model;
    }

    public void setSetting1(String string) {
        model.setString("setting1", string);
        viewer.setSetting1(getSetting1());
    }

    public String getSetting1() {
        return model.getString("setting1", "null");
    }

}

