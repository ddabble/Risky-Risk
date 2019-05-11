package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.presenter.SettingsPresenter;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.MainMenuView;
import no.ntnu.idi.tdt4240.view.SignInView;
import no.ntnu.idi.tdt4240.view.TutorialView;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;


// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private MainMenuView mainMenuView;
    private TutorialView tutorialView;
    private GameView gameView;
    private SignInView signinView;
    public IGPGSClient gpgsClient;

    public RiskyRisk() {
    }

    // Init needs to be called after we set the games GPGS client
    public void init () {
        mainMenuView = new MainMenuView(this);
        tutorialView = new TutorialView(this);
        gameView = new GameView();
        signinView = null;

        if (gpgsClient != null) {
            signinView = new SignInView(this);
        }
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

            case SIGNIN:
                setScreen(signinView);
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
        SIGNIN,
    }
}
