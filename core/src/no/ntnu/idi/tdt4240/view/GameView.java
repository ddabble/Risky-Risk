package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.controller.GameViewer;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.util.BoardInputProcessor;
import no.ntnu.idi.tdt4240.util.UIInputProcessor;

public class GameView extends AbstractView implements GameViewer {
    public static final float VIEWPORT_WIDTH = 1227; // TODO: temporary viewport size
    public static final float VIEWPORT_HEIGHT = 601;

    private OrthographicCamera camera;
    private final GameController gameController;
    private final BoardView boardView;
    private final PhaseView phaseView;
    private final TroopView troopView;

    public GameView(RiskyRisk game) {
        super(game);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        phaseView = new PhaseView(game);
        gameController = new GameController(this, game.getGameModel());

        boardView = new BoardView(camera, game);
        troopView = new TroopView(boardView.model.TERRITORY_MAP);
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
        multiplexer.addProcessor(new UIInputProcessor(phaseView, camera));
        multiplexer.addProcessor(new BoardInputProcessor(boardView, troopView, camera));
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
}
