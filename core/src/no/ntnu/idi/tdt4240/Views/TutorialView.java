package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.Controllers.Controller;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class TutorialView extends AbstractView {

    private final Controller controller;
    private Stage stage;
    private RiskyRisk game;
    Texture background;

    public TutorialView(RiskyRisk game) {
        super(game);
        this.game = game;
        background = new Texture("background.png");
        this.controller = new Controller();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Button backButton = this.createButton("Back to main");
        backButton.setPosition(100, 100);
        backButton.setSize(100,50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuView(game));
            }
        });

        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        game.batch.begin();
        game.font.draw(game.batch, "Welcome to the TutorialView!!! ", 100, 150);
        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
