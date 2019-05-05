package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.controller.GameViewer;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.BoardInputProcessor;
import no.ntnu.idi.tdt4240.util.UIInputProcessor;

public class GameView extends AbstractView implements GameViewer {
    public static final float VIEWPORT_WIDTH = 1227; // TODO: temporary viewport size
    public static final float VIEWPORT_HEIGHT = 601;

    private OrthographicCamera camera;
    private final GameController gameController;

    //Pseudoviews -- they all are a part of the GameView
    private final BoardView boardView;
    private final PhaseView phaseView;
    private final TroopView troopView;

    public GameView(RiskyRisk game) {
        super(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        //Pseudoviews -- they all are a part of the GameView
        phaseView = new PhaseView(game);
        troopView = new TroopView();
        boardView = new BoardView(camera, game);

        // TODO: Problem. The view uses the controller to relay UI clicks,
        // but the controller needs to initialize the view with data...
        gameController = new GameController(this, game.getGameModel());

        setUpInputProcessors();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    @Override
    public void show() {
        boardView.create();
        troopView.create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();

        boardView.render();
        troopView.render();
        phaseView.render(delta);

    }

    private void setUpInputProcessors() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new UIInputProcessor(phaseView, camera, gameController));
        multiplexer.addProcessor(new BoardInputProcessor(camera, gameController));
        Gdx.input.setInputProcessor(multiplexer);

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
        this.dispose();
    }

    @Override
    public void dispose() {
        boardView.dispose();
        troopView.dispose();
    }

    @Override
    public void setNumberOfPlayers(int num) {

    }

    public void initializeBoard(List<Territory> territories) {
        troopView.createCircles(territories);
    }

    public void setPlayerColorLookup(float[] playerColorLookup){
        boardView.playerColorLookup = playerColorLookup;
    }
    public void setMapSprite(Sprite mapSprite) {
        boardView.mapSprite = mapSprite;
    }

    // Update functions for controller
    public void territorySelected(Territory t) {
        this.troopView.onSelectTerritory(t);
    }
    public void updateTerritoryTroops(Territory t) {
        this.troopView.onTerritoryChangeNumTroops(t);
    }
    public void updatePhase(String curPhase, String nextPhase) {
        this.phaseView.updatePhase(curPhase, nextPhase);
    }


}
