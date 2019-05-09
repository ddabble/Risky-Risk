package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsController {
    public static final SettingsController INSTANCE = new SettingsController();

//    private Collection<SettingsObserver> observers = new ArrayList<>();

    // TODO: Create SettingsView class
//    private final SettingsView view;

    private SettingsController() {
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

//    public static void addObserver(SettingsObserver observer) {
//        INSTANCE.observers.add(observer);
//    }
}

