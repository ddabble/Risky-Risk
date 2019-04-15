package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.Controllers.IGPGSClient;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class MainMenuView extends AbstractView {
    private Table table;
    private IGPGSClient gpgsClient;
    OrthographicCamera camera;
    Texture background;
    private Stage stage;
    private Label nameField;

    public MainMenuView(RiskyRisk game) {
        super(game);
        //background = new Texture("background.png");
        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, 800, 480);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        gpgsClient = game.gpgsClient;


    }

    @Override
    public void show() {

        Button signOutButton = this.createButton("Sign out");
        Button checkGamesButton = this.createButton("Check active games");
        Button startMatchButton = this.createButton("Start new match");

        // Sign out
        signOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.signOut();
                    game.setScreen(new SignInView(game));
                }
            }
        });


        startMatchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onStartMatchClicked();
                }
            }
        });

        checkGamesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onCheckGamesClicked();
                }
            }
        });

        table.add(signOutButton).pad(100);
        table.add(startMatchButton).pad(100);
        table.add(checkGamesButton).pad(100);
        table.row();



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


        Button tutorialButton = this.createButton("Tutorial");
        tutorialButton.setPosition(100,400);
        tutorialButton.setSize(100,50);

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TutorialView(game));
            }
        });


        //stage.addActor(multiplayerButton);
        //stage.addActor(tutorialButton);
    }

    @Override
    public void render(float delta) {

        if (gpgsClient.matchActive()) {
            game.setScreen(new RiskyView(game));
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        //stage.getBatch().draw(background, 0, 0);
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
