package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import no.ntnu.idi.tdt4240.observer.GameObserver;
import no.ntnu.idi.tdt4240.presenter.GamePresenter;

public class GameView implements GameObserver, Screen {
    public static final float VIEWPORT_WIDTH = 1227; // TODO: temporary viewport size
    public static final float VIEWPORT_HEIGHT = 601;

    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    private OrthographicCamera camera;

    public GameView() {
        GamePresenter.addObserver(this);

        camera = new OrthographicCamera();

        phaseView = new PhaseView(camera);
        boardView = new BoardView(camera);
        troopView = new TroopView();
    }

    @Override
    public void show() {
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);

        GamePresenter.INSTANCE.init();
        setInputProcessors();

        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
    }

    private void setInputProcessors() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        // Note: the input processors added first get to handle input first
        multiplexer.addProcessor(phaseView.getStage());
        boardView.setInputProcessors(multiplexer);
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
        GamePresenter.INSTANCE.reset();
    }

    @Override
    public void dispose() {

    }
}
