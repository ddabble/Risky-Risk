package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.presenter.SettingsPresenter;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.MainMenuView;
import no.ntnu.idi.tdt4240.view.SignInView;
import no.ntnu.idi.tdt4240.view.TutorialView;
import no.ntnu.idi.tdt4240.view.WinView;


// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private MainMenuView mainMenuView;
    private TutorialView tutorialView;
    private WinView winView;
    private GameView gameView;
    private SignInView signInView;
    public IGPGSClient gpgsClient;

    public RiskyRisk() {
    }

    // Init needs to be called after we set the games GPGS client
    public void init() {

        mainMenuView = new MainMenuView(this);
        tutorialView = new TutorialView(this);
        winView = new WinView(this);
        gameView = new GameView(this);
        signInView = null;

        //gpgsClient needs several callbacks to be hooked up to properly function
        //these are added here and inside signInView.
        if (gpgsClient != null) {
            //create the signInView, this happens here because it requires a gpgsClient
            signInView = new SignInView(this);

            //register a callback for starting the game ui when receiving match data
            //TODO: this is currently not used, instead we just check matchActive() in main
            //menu every frame
            gpgsClient.setGameUIStartHandler(new IGPGSClient.GameUIStartHandler() {
                @Override
                public void onGameUIStart() {
                    //setScreen(ScreenEnum.GAME);
                }
            });
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

            case SIGN_IN:
                setScreen(signInView);
                break;
            case WIN:
                setScreen(winView);
                break;

        }
    }

    @Override
    public void create() {
        TerritoryModel.init();
        SettingsPresenter.INSTANCE.init();

        // TODO: set number of players from (settings) menu
        //SettingsPresenter.INSTANCE.setNumPlayers(8);

        switch (Gdx.app.getType()) {
            case Android: // android specific code
                setScreen(ScreenEnum.MAIN_MENU);
                break;
            case Desktop: // desktop specific code
                setScreen(ScreenEnum.MAIN_MENU);
                break;
            default:
                setScreen(ScreenEnum.SIGN_IN);
        }
    }

    public enum ScreenEnum {
        MAIN_MENU,
        TUTORIAL,
        GAME,
        SIGN_IN,
        WIN,
    }
}
