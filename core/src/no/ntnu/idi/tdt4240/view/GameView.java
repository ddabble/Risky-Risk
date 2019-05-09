package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.util.BoardInputProcessor;
import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GameView implements GameObserver, Screen {
    public static final float VIEWPORT_WIDTH = 1227; // TODO: temporary viewport size
    public static final float VIEWPORT_HEIGHT = 601;

    private OrthographicCamera camera;

    // Pseudo-views - they all are a part of the GameView
    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    public GameView(RiskyRisk game) {
        super(game);
        GameController.addObserver(this);

        camera = new OrthographicCamera();

        // Pseudo-views - they all are a part of the GameView
        phaseView = new PhaseView(game);
        boardView = new BoardView(camera);
        troopView = new TroopView();
    }

    /*
    public void setNumberOfPlayers(int num) {

    }
    */

    public void updatePhase(String curPhase, String nextPhase) {
        phaseView.updatePhase(curPhase, nextPhase);
    }

    @Override
    public void show() {
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        boardView.create(mapTexture, TerritoryModel.getTerritoryMap());
        troopView.create(TerritoryModel.getTerritoryMap(), circleTexture, circleSelectTexture);
        phaseView.show();

        setUpInputProcessors();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    private void setUpInputProcessors() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // Note: the input processors added first get to handle input first
        multiplexer.addProcessor(phaseView.getStage());
        multiplexer.addProcessor(new BoardInputProcessor(boardView, camera));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();

        boardView.render();
        troopView.render();
        phaseView.render();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        phaseView.dispose();
        troopView.dispose();
        boardView.dispose();
    }

    @Override
    public void dispose() {

    }
}
