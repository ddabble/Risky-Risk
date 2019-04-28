package no.ntnu.idi.tdt4240;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.idi.tdt4240.EntitySystems.BoardSystem;
import no.ntnu.idi.tdt4240.Models.GameModel;
import no.ntnu.idi.tdt4240.Models.SettingsModel;
import no.ntnu.idi.tdt4240.Views.MainMenuView;

// Switches between App states, loads shared resources
public class RiskyRisk extends Game {
    public static final float VIEWPORT_WIDTH = 1227; // temporary
    public static final float VIEWPORT_HEIGHT = 601;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private BoardSystem boardSystem;

    private SettingsModel settingsModel;

    public SettingsModel getSettingsModel() {
        return settingsModel;
    }

    private GameModel gameModel;

    public GameModel getGameModel() {
        return gameModel;
    }

    @Override
    public void create() {
        settingsModel = new SettingsModel();
        gameModel = new GameModel();
        this.setScreen(new MainMenuView(this));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        batch = new SpriteBatch();

        boardSystem = new BoardSystem(camera);
        boardSystem.init();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        camera.update();

        boardSystem.render(camera);

//        batch.setProjectionMatrix(camera.combined);
//        batch.begin();
//        batch.end();
    }

    @Override
    public void dispose() {
        boardSystem.dispose();
    }
}
