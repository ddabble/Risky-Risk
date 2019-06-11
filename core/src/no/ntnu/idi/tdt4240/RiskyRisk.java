package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import no.ntnu.idi.tdt4240.audio.MusicController;
import no.ntnu.idi.tdt4240.client.IGPGSClient;
import no.ntnu.idi.tdt4240.model.SettingsModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.MainMenuView;
import no.ntnu.idi.tdt4240.view.SignInView;
import no.ntnu.idi.tdt4240.view.StartOfflineView;
import no.ntnu.idi.tdt4240.view.TutorialView;
import no.ntnu.idi.tdt4240.view.WinView;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private final IGPGSClient gpgsClient;

    private final MainMenuView mainMenuView;
    private final TutorialView tutorialView;
    private final WinView winView;
    private final StartOfflineView startOfflineView;
    private final GameView gameView;
    private final SignInView signInView;

    public RiskyRisk(IGPGSClient gpgsClient) {
        this.gpgsClient = gpgsClient;

        mainMenuView = new MainMenuView(this, gpgsClient);
        tutorialView = new TutorialView(this);
        winView = new WinView(this);
        startOfflineView = new StartOfflineView(this, gpgsClient);
        gameView = new GameView(this, gpgsClient); // the game should not be handing out the client, instead the
        // client should be its own singleton model

        //gpgsClient needs several callbacks to be hooked up to properly function
        //these are added here and inside signInView.
        if (gpgsClient != null) {
            //create the signInView, this happens here because it requires a gpgsClient
            signInView = new SignInView(this, gpgsClient);

            //register a callback for starting the game ui when receiving match data
            //TODO: this is currently not used, instead we just check matchActive() in main
            //menu every frame
            gpgsClient.setGameUIStartHandler(new IGPGSClient.GameUIStartHandler() {
                @Override
                public void onGameUIStart() {
                    //setScreen(ScreenEnum.GAME);
                }
            });
        } else
            signInView = null;
    }

    public void setScreen(ScreenEnum screen) {
        switch (screen) {
            case MAIN_MENU:
                setScreen(mainMenuView);
                break;

            case TUTORIAL:
                setScreen(tutorialView);
                break;

            case START_OFFLINE:
                setScreen(startOfflineView);
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
        /*
        Prevents exiting to the home screen when the back button is pressed,
        which for some reason also makes the app restart when resumed, but without clearing all memory.
        It's also currently used by the main menu sub-screens to go back to the main menu.
         */
        Gdx.input.setCatchBackKey(true);

        TerritoryModel.init();
        SettingsModel.init();
        UIStyle.init();

        // Calls `show()` on views
        switch (Gdx.app.getType()) {
            case Android:
                setScreen(ScreenEnum.MAIN_MENU);
                break;

            case Desktop:
                setScreen(ScreenEnum.MAIN_MENU);
                break;

            default:
                setScreen(ScreenEnum.SIGN_IN);
        }
    }

    @Override
    public void dispose() {
        // Calls `hide()` on active view
        super.dispose();
        MusicController.dispose();
        UIStyle.dispose();
    }

    public enum ScreenEnum {
        MAIN_MENU,
        TUTORIAL,
        START_OFFLINE,
        GAME,
        SIGN_IN,
        WIN,
    }
}
