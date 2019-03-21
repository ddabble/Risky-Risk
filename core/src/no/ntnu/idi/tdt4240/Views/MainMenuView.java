package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;

public class MainMenuView extends AbstractView {
    OrthographicCamera camera;
    Texture background;
    private Stage stage;

    public MainMenuView(RiskyRisk game) {
        super(game);
        background = new Texture("background.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);


        // Back Button
        Button multiplayerButton = this.createButton("Play");
        multiplayerButton.setPosition(100, 200);
        multiplayerButton.setSize(100,50);

        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiplayerView(game));
            }
        });

        Button quickplayButton = this.createButton("Quickplay");
        quickplayButton.setPosition(100,100);
        quickplayButton.setSize(100,50);

        quickplayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new QuickplayView(game));
            }
        });

        // Save Button
        Button settingsButton = this.createButton("Settings");
        settingsButton.setPosition(100,300);
        settingsButton.setSize(100,50);

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsView(game));
            }
        });

        Button tutorialButton = this.createButton("Tutorial");
        tutorialButton.setPosition(100,400);
        tutorialButton.setSize(100,50);

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TutorialView(game));
            }
        });

        stage.addActor(multiplayerButton);
        stage.addActor(quickplayButton);
        stage.addActor(settingsButton);
        stage.addActor(tutorialButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
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

    }

    @Override
    public void dispose() {

    }
}
