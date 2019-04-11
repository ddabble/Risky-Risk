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

import no.ntnu.idi.tdt4240.IGPGSClient;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class MultiplayerView extends AbstractView {
    OrthographicCamera camera;
    Texture background;
    private Stage stage;
    private Table table;
    private IGPGSClient gpgsClient;
    private Label player;

    public MultiplayerView(RiskyRisk game) {
        super(game);
        background = new Texture("background.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

    }
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);


        //GPGSTest
        // Sign in and out
        player = this.createLabel("foo");
        table.add(player).pad(100);

        table.row();

        gpgsClient = game.gpgsClient;


        Button signInButton = this.createButton("Sign in");

        signInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.startSignInIntent();
                }
            }
        });

        Button signOutButton = this.createButton("Sign out");

        signOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.signOut();
                }
            }
        });


        table.add(signInButton).pad(100);
        table.add(signOutButton).pad(100);
        table.row();

        // Turns
        Button quickMatchButton = this.createButton("Quickmatch");

        quickMatchButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onStartMatchClicked();
                }
            }
        });

        table.add(quickMatchButton).pad(100);
        table.row();

        // Back
        Button backButton = this.createButton("Back to main");

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuView(game));
            }
        });

        table.add(backButton).pad(100);




    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (gpgsClient != null) {
            player.setText(gpgsClient.getPlayerDisplayName());
        }


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
