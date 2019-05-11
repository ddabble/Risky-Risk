package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.presenter.SettingsPresenter;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.MainMenuView;
import no.ntnu.idi.tdt4240.view.TutorialView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private final MainMenuView mainMenuView;
    private final TutorialView tutorialView;
    private final GameView gameView;

    public RiskyRisk() {
        mainMenuView = new MainMenuView(this);
        tutorialView = new TutorialView(this);
        gameView = new GameView();
    }

    public void setScreen(ScreenEnum screen) {
        // TODO: add other screens
        switch (screen) {
            case MAIN_MENU:
                setScreen(mainMenuView);
                break;

            case TUTORIAL:
                setScreen(tutorialView);
                break;

            case GAME:
                setScreen(gameView);
                break;
        }
    }

    @Override
    public void create() {
        TerritoryModel.init();
        SettingsPresenter.INSTANCE.init();

        // TODO: set number of players from (settings) menu
        SettingsPresenter.INSTANCE.setNumPlayers(8);

        setScreen(ScreenEnum.MAIN_MENU);
    }

    public enum ScreenEnum {
        MAIN_MENU,
        TUTORIAL,
        GAME,
    }
}
