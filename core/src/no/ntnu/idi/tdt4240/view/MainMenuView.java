package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.observer.MenuObserver;
import no.ntnu.idi.tdt4240.presenter.MenuPresenter;

public class MainMenuView extends AbstractView implements MenuObserver, Screen {
    private final RiskyRisk game;

    private OrthographicCamera camera;
    private Texture background;
    private Stage stage;

    private Table table;
    private IGPGSClient gpgsClient;

    public MainMenuView(RiskyRisk game) {

        gpgsClient = game.gpgsClient;
        MenuPresenter.addObserver(this);
        this.game = game;
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        super.create();

        BoardModel.INSTANCE.setGPGSClient(gpgsClient);

        camera.setToOrtho(false, 800, 480);
        background = new Texture("background.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        Button signOutButton = createButton("Sign out");
        Button signInButton = createButton("Sign in");
        Button checkGamesButton = createButton("Check active games");
        Button startMatchButton = createButton("Start new match");
        Button tutorialButton = createButton("Tutorial");

        Button offlineButton = createButton("Offline Game");

        // Sign out
        offlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.GAME);
            }
        });

        // Sign out
        signOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.signOut();
                    game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
                }
            }
        });

        //sign in
        signInButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.SIGNIN);
            }
        });

        // Start match
        startMatchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onStartMatchClicked();
                }
            }
        });

        // Check matches
        checkGamesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onCheckGamesClicked();
                }
            }
        });

        // Tutorial
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.TUTORIAL);
            }
        });

        table.add(tutorialButton).pad(100);
        table.row();
        table.add(offlineButton).pad(100);
        table.row();
        if(gpgsClient != null && gpgsClient.isSignedIn()) {
            table.add(signOutButton).pad(100);
        } else {
            table.add(signInButton).pad(100);
        }
        table.add(startMatchButton).pad(100);
        table.add(checkGamesButton).pad(100);
        table.row();
    }

    @Override
    public void render(float delta) {

        if (gpgsClient != null && gpgsClient.matchActive()) {
            game.setScreen(RiskyRisk.ScreenEnum.GAME);
        }

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
