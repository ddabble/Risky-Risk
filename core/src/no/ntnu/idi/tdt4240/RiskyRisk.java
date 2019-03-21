package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;

import no.ntnu.idi.tdt4240.Models.GameModel;
import no.ntnu.idi.tdt4240.Models.SettingsModel;
import no.ntnu.idi.tdt4240.Views.MainMenuView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {

    private SettingsModel settingsModel;
    public SettingsModel getSettingsModel() {
        return settingsModel;
    }
    private GameModel gameModel;
    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void create () {
        settingsModel = new SettingsModel();
        gameModel = new GameModel();
        this.setScreen(new MainMenuView(this));
    }

    @Override
    public void render () {
        super.render();
    }

    @Override
    public void dispose() {
    }
}
