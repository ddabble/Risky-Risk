package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class SignInView extends ScreenAdapter {
    private final RiskyRisk game;
    private final IGPGSClient gpgsClient;
    private final OrthographicCamera camera;

    private Stage stage;
    private Table table;

    private BitmapFont buttonFont;

    private boolean isSignedIn = false;

    public SignInView(RiskyRisk game, IGPGSClient gpgsClient) {
        this.game = game;
        camera = new OrthographicCamera();
        this.gpgsClient = gpgsClient;

        //set the sign in attempt handler to handle what happens if sign in fails or succeeds
        gpgsClient.setSignInAttemptHandler(new IGPGSClient.SignInAttemptHandler() {
            @Override
            public void onSuccess() {
                //we can not actually switch scenes here because the return from an intent might
                //happen at a time where LibGDX doesn't expect it and shaders wont compile correctly
                //i think???? So instead we need to set a flag that we check every frame
                //game.setScreen(new MainMenuView(game));
                isSignedIn = true;
            }

            @Override
            public void onFailure() {
                //failed to sign in, please try again );
                isSignedIn = false;
            }
        });
    }

    @Override
    public void show() {
        isSignedIn = false;
        camera.setToOrtho(false, 800, 480);
        stage = new Stage(new StretchViewport(800, 480, camera));
        table = new Table();
        //table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        //check if we are already signed in
        if (gpgsClient.isSignedIn()) {
            isSignedIn = true;
        } else { //show sign in button
            buttonFont = UIStyle.INSTANCE.createStandardButtonFont();
            float heightRatio = stage.getHeight() / Gdx.graphics.getHeight();
            buttonFont.getData().setScale(heightRatio);
            Button signInButton = UIStyle.INSTANCE.createTextButton("Sign in to Google Play", buttonFont);

            signInButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (gpgsClient != null) {
                        gpgsClient.startSignInIntent();
                    }
                }
            });
            table.add(signInButton).width(350).height(150).pad(100);
            table.row();
        }

        MainMenuView.setInputProcessors_mainMenuSubViews(stage, game);

        Color backgroundColor = MainMenuView.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();

        if (isSignedIn) {
            game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
        }
    }

    @Override
    public void hide() {
        Gdx.input.setCatchBackKey(false);

        if (buttonFont != null)
            buttonFont.dispose();

        table.clear();
        stage.dispose();
        super.dispose();
    }
}
