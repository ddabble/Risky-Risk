package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
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

public class SignInView extends AbstractView {
    OrthographicCamera camera;
    Texture background;
    private Stage stage;
    private Table table;
    private IGPGSClient gpgsClient;


    public SignInView(RiskyRisk game) {
        super(game);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        //GPGSTest
        gpgsClient = game.gpgsClient;

    }
    @Override
    public void show() {
        if (gpgsClient.isSignedIn()) {
            game.setScreen(new MainMenuView(this.game));
        }


        Button signInButton = this.createButton("Connect to google play");
        signInButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.startSignInIntent();
                    game.setScreen(new MainMenuView(game));
                }
            }
        });


        table.add(signInButton).pad(100);
        table.row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        //stage.getBatch().draw(background, 0, 0);
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
