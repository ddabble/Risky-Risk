package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.MenuController;
import no.ntnu.idi.tdt4240.observer.MenuObserver;

public class MainMenuView extends AbstractView implements MenuObserver, Screen {
    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;

    public MainMenuView(RiskyRisk game) {
        super(game);
        MenuController.addObserver(this);
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        super.create();

        camera.setToOrtho(false, 800, 480);
        background = new Texture("background.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Back Button
        Button multiplayerButton = createButton("Play");
        multiplayerButton.setPosition(100, 200);
        multiplayerButton.setSize(100, 50);

        multiplayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Should be MultiplayerView, GameView is only temporary to easier see the board being rendered
//                game.setScreen(new MultiplayerView(game));
                game.setScreen(RiskyRisk.ScreenEnum.GAME);
            }
        });

        Button tutorialButton = createButton("Tutorial");
        tutorialButton.setPosition(100, 400);
        tutorialButton.setSize(100, 50);

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TutorialView(game));
            }
        });

        stage.addActor(multiplayerButton);
        stage.addActor(tutorialButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {
        background.dispose();
        stage.dispose();
        super.dispose();
    }
}
