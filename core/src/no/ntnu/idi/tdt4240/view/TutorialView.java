package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;

public class TutorialView extends AbstractView {
    private Stage stage;
    private Texture background;

    public TutorialView(RiskyRisk game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        background = new Texture("background.png");

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Button backButton = createButton("Back to main");
        backButton.setPosition(100, 100);
        backButton.setSize(100, 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuView(game));
            }
        });

        stage.addActor(backButton);
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
    public void hide() {
        background.dispose();
        stage.dispose();
        super.hide();
    }
}
