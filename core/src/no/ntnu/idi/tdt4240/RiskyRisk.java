package no.ntnu.idi.tdt4240;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import no.ntnu.idi.tdt4240.EntitySystems.BoardSystem;

public class RiskyRisk extends ApplicationAdapter {
    public static final float VIEWPORT_WIDTH = 1227; // temporary
    public static final float VIEWPORT_HEIGHT = 601;

    private Engine engine;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private BoardSystem boardSystem;

    @Override
    public void create() {
        engine = new Engine();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        batch = new SpriteBatch();

        boardSystem = new BoardSystem(camera);
        engine.addSystem(boardSystem);

        Gdx.gl.glClearColor(0, 0, 0, 1);
    }

    @Override
    public void render() {
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
        batch.dispose();
    }
}
