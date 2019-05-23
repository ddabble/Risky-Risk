package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.observer.MenuObserver;
import no.ntnu.idi.tdt4240.presenter.MenuPresenter;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class MainMenuView extends ApplicationAdapter implements MenuObserver, Screen {
    public static final Color BACKGROUND_COLOR = new Color(0xE9E6B9FF);

    private final RiskyRisk game;
    private final IGPGSClient gpgsClient;
    private final OrthographicCamera camera;

    private Music mainMenuTheme;
    private Texture background;
    private Stage stage;
    private Table table;

    private BitmapFont buttonFont;

    public MainMenuView(RiskyRisk game) {
        MenuPresenter.addObserver(this);
        this.game = game;
        gpgsClient = game.gpgsClient;
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {
        BoardModel.INSTANCE.setGPGSClient(gpgsClient);
        TurnModel.INSTANCE.setGPGSClient(gpgsClient);

        camera.setToOrtho(false, 800, 480);
        stage = new Stage(new StretchViewport(800, 480, camera));
        background = new Texture("background.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.setX(0);
        table.setY(0);

        createButtons();

        stage.addActor(table);


        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
    }

    private void createButtons() {
        buttonFont = UIStyle.INSTANCE.createStandardButtonFont();
        float heightRatio = stage.getHeight() / Gdx.graphics.getHeight();
        buttonFont.getData().setScale(heightRatio);

        Button signOutButton = UIStyle.INSTANCE.createTextButton("Sign out", buttonFont);
        Button signInButton = UIStyle.INSTANCE.createTextButton("Sign in", buttonFont);
        Button checkGamesButton = UIStyle.INSTANCE.createTextButton("Check active games", buttonFont);
        Button startMatchButton = UIStyle.INSTANCE.createTextButton("Start new match", buttonFont);
        Button tutorialButton = UIStyle.INSTANCE.createTextButton("Tutorial", buttonFont);

        Button offlineButton = UIStyle.INSTANCE.createTextButton("Offline Game", buttonFont);

        // Sign out
        offlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainMenuTheme.dispose();
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
                game.setScreen(RiskyRisk.ScreenEnum.SIGN_IN);
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


        table.add(offlineButton).width(150).height(50).pad(20);
        table.row();
        table.add(tutorialButton).width(150).height(50).pad(20);
        table.row();
        if (gpgsClient != null && gpgsClient.isSignedIn()) {
            table.add(startMatchButton).width(150).height(50).pad(20);
            table.row();
            table.add(checkGamesButton).width(150).height(50).pad(20);
            table.row();
            table.add(signOutButton).width(150).height(50).pad(20);
        } else {
            table.add(signInButton).width(150).height(50).pad(20);
        }
        mainMenuTheme = Gdx.audio.newMusic(Gdx.files.internal("menutheme.mp3"));
        mainMenuTheme.setLooping(true);
        mainMenuTheme.play();

        //table.row();
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
        buttonFont.dispose();
        table.clear();
        stage.dispose();
        background.dispose();
        super.dispose();
    }
}
