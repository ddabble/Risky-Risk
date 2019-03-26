package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.stream.IntStream;

import no.ntnu.idi.tdt4240.Controllers.GameController;
import no.ntnu.idi.tdt4240.Controllers.GameViewer;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class QuickplayView extends AbstractView implements GameViewer{
    OrthographicCamera camera;
    Texture background;
    private Stage stage;
    private GameController gameController;
    private Label numberOfPlayersLabel;

    public QuickplayView(RiskyRisk game) {
        super(game);
        background = new Texture("background.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        gameController = new GameController(this, game.getGameModel());
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        numberOfPlayersLabel = createLabel("2 player game");
        numberOfPlayersLabel.setPosition(100, 300);
        stage.addActor(numberOfPlayersLabel);

        Integer[] options = new Integer[] {2, 3, 4, 5, 6};
        final SelectBox select = createSelectBox(options);
        select.setPosition(100, 350);
        select.setWidth(100);
        select.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                gameController.setNumberOfPlayers((Integer)select.getSelected());
            }
        });
        select.setSelected(gameController.getNumberOfPlayers());
        stage.addActor(select);

        Button startButton = createButton("Start game!");
        startButton.setPosition(100, 250);
        startButton.setSize(100, 50);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new RiskyView(game));
            }
        });
        stage.addActor(startButton);

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
    public void setNumberOfPlayers(int num) {
        numberOfPlayersLabel.setText(num + " player game");
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
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
