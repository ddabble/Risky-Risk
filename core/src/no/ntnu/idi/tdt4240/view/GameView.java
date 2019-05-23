package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.observer.GameObserver;
import no.ntnu.idi.tdt4240.presenter.GamePresenter;

public class GameView implements GameObserver, Screen {
    private final RiskyRisk game;
    private static final float WORLD_WIDTH = 100;
    private static final Color BACKGROUND_COLOR = new Color(0xBBD3F9 << 8);

    private final IGPGSClient client;

    private Music gameThemeMusic;

    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;
    private final LeaderboardView leaderboardView;

    private OrthographicCamera camera;

    public GameView(RiskyRisk game, IGPGSClient client) {
        this.game = game; // need this for exiting back to main menu
        GamePresenter.addObserver(this);
        this.client = client;

        boardView = new BoardView();
        troopView = new TroopView(boardView);
        phaseView = new PhaseView(boardView);
        leaderboardView = new LeaderboardView();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, getWorldWidth(), getWorldHeight());

        gameThemeMusic = Gdx.audio.newMusic(Gdx.files.internal("gametheme.mp3"));
        GamePresenter.init(camera, client);
        setInputProcessors();

        gameThemeMusic.setLooping(true);
        gameThemeMusic.play();
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, 1);
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
        leaderboardView.render();
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
        gameThemeMusic.dispose();
        GamePresenter.reset();
    }

    @Override
    public void dispose() {
        gameThemeMusic.dispose();
    }

    @Override
    public void exitToMainMenu() {
        game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
    }

    @Override
    public void exitToWinScreen() {
        game.setScreen(RiskyRisk.ScreenEnum.WIN);
    }

    public static float getWorldWidth() {
        return WORLD_WIDTH;
    }

    /**
     * Requires LibGDX to be initialized to work.
     */
    public static float getWorldHeight() {
        final float aspectRatio = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        return WORLD_WIDTH * aspectRatio;
    }
}
