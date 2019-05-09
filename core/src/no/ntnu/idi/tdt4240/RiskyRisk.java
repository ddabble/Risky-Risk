package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.controller.SettingsController;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.view.MainMenuView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private final MainMenuView mainMenuView;

    public RiskyRisk() {
        mainMenuView = new MainMenuView(this);
    }

    public void setScreen(ScreenEnum screen) {
        // TODO: add other screens
        switch (screen) {
            case MAIN_MENU:
                // TODO: should be mainMenuController
                setScreen(mainMenuView);
                break;

            case GAME:
                setScreen(GameController.INSTANCE);
                break;
        }
    }

    @Override
    public void create() {
        TerritoryModel.init();
        SettingsController.INSTANCE.init();
        setScreen(ScreenEnum.MAIN_MENU);
    }

    public enum ScreenEnum {
        MAIN_MENU,
        GAME,
    }
}
