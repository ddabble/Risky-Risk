package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.SettingsModel;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.SignInView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private SettingsModel settingsModel;
    private GameModel gameModel;
    public IGPGSClient gpgsClient;
    private GameView gameView;

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }



    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void create() {
        settingsModel = new SettingsModel();
        gameModel = new GameModel();
        gameModel.init();
        this.setScreen(new SignInView(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        gameModel.reset();
        super.dispose();
    }
}
