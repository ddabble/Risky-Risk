package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.observer.MenuObserver;
import no.ntnu.idi.tdt4240.presenter.MenuPresenter;
import no.ntnu.idi.tdt4240.sound.MusicController;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class MainMenuView extends ScreenAdapter implements MenuObserver {
    public static final Color BACKGROUND_COLOR = new Color(0xE9E6B9FF);

    private final RiskyRisk game;
    private final IGPGSClient gpgsClient;

    private Texture background;
    private Stage stage;
    private Table table;

    private BitmapFont buttonFont;

    private Texture unmuteIcon;
    private Texture muteIcon;
    private TextureRegionDrawable muteButtonImage;

    public MainMenuView(RiskyRisk game, IGPGSClient gpgsClient) {
        this.game = game;
        this.gpgsClient = gpgsClient;

        MenuPresenter.addObserver(this);
    }

    @Override
    public void show() {
        stage = new Stage(new StretchViewport(800, 480));
        background = new Texture("background.png");
        background.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);

        unmuteIcon = new Texture(Gdx.files.internal("octicons/unmute.png"));
        muteIcon = new Texture(Gdx.files.internal("octicons/mute.png"));
        if (MusicController.INSTANCE.isMuted())
            muteButtonImage = new TextureRegionDrawable(muteIcon);
        else
            muteButtonImage = new TextureRegionDrawable(unmuteIcon);

        table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        table.setPosition(0, 0);

        createButtons();
        stage.addActor(table);

        MusicController.INSTANCE.playMainMenuTheme();

        Gdx.input.setInputProcessor(stage);
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
    }

    private void createButtons() {
        final float buttonWidth = 150;
        final float buttonHeight = 50;
        final float buttonPadding = 20;

        buttonFont = UIStyle.INSTANCE.createStandardButtonFont();
        final float heightRatio = stage.getHeight() / Gdx.graphics.getHeight();
        buttonFont.getData().setScale(heightRatio);

        Button signOutButton = UIStyle.INSTANCE.createTextButton("Sign out", buttonFont);
        Button signInButton = UIStyle.INSTANCE.createTextButton("Sign in", buttonFont);
        Button checkGamesButton = UIStyle.INSTANCE.createTextButton("Check active games", buttonFont);
        Button startMatchButton = UIStyle.INSTANCE.createTextButton("Start new match", buttonFont);
        Button tutorialButton = UIStyle.INSTANCE.createTextButton("Tutorial", buttonFont);

        Button offlineButton = UIStyle.INSTANCE.createTextButton("Offline Game", buttonFont);

        final float muteButtonSize = buttonHeight * 1.25f;
        ImageButton muteButton = UIStyle.INSTANCE.createImageButton(muteButtonImage, muteButtonSize / 10f);
        muteButton.setSize(muteButtonSize, muteButtonSize);
        muteButton.setPosition(stage.getWidth() - buttonPadding, stage.getHeight() - buttonPadding, Align.topRight);

        // Sign out
        offlineButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.START_OFFLINE);
            }
        });

        // Sign out
        signOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.signOut();
                    // Reload main menu to update buttons
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

        muteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (MusicController.INSTANCE.isMuted()) {
                    MusicController.INSTANCE.unmute();
                    muteButtonImage.setRegion(new TextureRegion(unmuteIcon));
                } else {
                    MusicController.INSTANCE.mute();
                    muteButtonImage.setRegion(new TextureRegion(muteIcon));
                }
            }
        });


        table.add(offlineButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
        table.row();
        table.add(tutorialButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
        table.row();
        if (gpgsClient != null && gpgsClient.isSignedIn()) {
            table.add(startMatchButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
            table.row();
            table.add(checkGamesButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
            table.row();
            table.add(signOutButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
        } else {
            table.add(signInButton).width(buttonWidth).height(buttonHeight).pad(buttonPadding);
        }

        stage.addActor(muteButton);
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
        muteIcon.dispose();
        unmuteIcon.dispose();
        buttonFont.dispose();
        table.clear();
        stage.dispose();
        background.dispose();
        super.dispose();
    }

    public static void setInputProcessors_mainMenuSubViews(Stage stage, final RiskyRisk game) {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK) {
                    game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
                    return true;
                }
                return false;
            }
        });
        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);
    }
}
