package no.ntnu.idi.tdt4240.presenter;

import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.model.SettingsModel;

public class SettingsPresenter {
    public static final SettingsPresenter INSTANCE = new SettingsPresenter();

    private SettingsPresenter() {}

    public void init() {
        SettingsModel.INSTANCE.init();
    }

    public void setNumPlayers(int num) {
        SettingsModel.INSTANCE.setNumPlayers(num);
        MultiplayerModel.INSTANCE.init(num);
    }
}

