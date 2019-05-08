package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.util.BoardInputProcessor;
import no.ntnu.idi.tdt4240.util.UIInputProcessor;

public class GameView extends AbstractView {
    public static final float VIEWPORT_WIDTH = 1227; // TODO: temporary viewport size
    public static final float VIEWPORT_HEIGHT = 601;

    private OrthographicCamera camera;
    private final GameController controller;

    // Pseudo-views - they all are a part of the GameView
    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    public GameView(GameController controller, RiskyRisk game) {
        super(game);

        // TODO: Problem. The view uses the controller to relay UI clicks,
        // but the controller needs to initialize the view with data...
        this.controller = controller;

        camera = new OrthographicCamera();

        // Pseudo-views - they all are a part of the GameView
        phaseView = new PhaseView(game, camera);
        boardView = new BoardView(controller, camera);
        troopView = new TroopView(controller);
    }

    public PhaseView getPhaseView() {
        return phaseView;
    }

    public BoardView getBoardView() {
        return boardView;
    }

    public TroopView getTroopView() {
        return troopView;
    }

    /*
    public void setNumberOfPlayers(int num) {

    }
    */

    public void updatePhase(String curPhase, String nextPhase) {
        phaseView.updatePhase(curPhase, nextPhase);
    }

    public void show(Texture mapTexture, Texture circleTexture, Texture circleSelectTexture) {
        super.show();

        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        setUpInputProcessors();

        boardView.create(mapTexture, TerritoryModel.getInstance().TERRITORY_MAP);
        troopView.create(TerritoryModel.getInstance().TERRITORY_MAP, circleTexture, circleSelectTexture);
        phaseView.show();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    private void setUpInputProcessors() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new UIInputProcessor(controller, phaseView, camera));
        multiplexer.addProcessor(new BoardInputProcessor(controller, boardView, camera));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        camera.update();

        boardView.render();
        troopView.render();
        phaseView.render(delta);
    }

    @Override
    public void hide() {
        phaseView.hide();
        troopView.dispose();
        boardView.dispose();
        super.hide();
    }
}
