package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.idi.tdt4240.Controllers.GameController;
import no.ntnu.idi.tdt4240.Controllers.GameViewer;
import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.Views.AbstractView;

public class RiskyView extends AbstractView implements GameViewer{

    OrthographicCamera camera;
    Texture img;
    private GameController gameController;

    public RiskyView(RiskyRisk game) {
        super(game);
        img = new Texture("badlogic.jpg");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        gameController = new GameController(this, game.getGameModel());
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
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
        img.dispose();
    }

    @Override
    public void setNumberOfPlayers(int num) {

    }
}
