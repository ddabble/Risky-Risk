package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.SettingsModel;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.MainMenuView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    private SettingsModel settingsModel;
    private GameModel gameModel;

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

//        gameView = new GameView(this);

        this.setScreen(new MainMenuView(this));
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
