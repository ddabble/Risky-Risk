package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.Controllers.Controller;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class SettingsView extends AbstractView {
    private final Controller controller;
    private Stage stage;
    private RiskyRisk game;

    public SettingsView(RiskyRisk game) {
        super(game);
        this.game = game;
        this.controller = new Controller();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Select Box
        // Options should be in Model?
        String[] options = new String[]{"Option 1", "Option 2", "Option 3"};
        SelectBox select = this.createSelectBox(options);
        select.setPosition(100,400);
        select.setWidth(100);
        select.setSelectedIndex(0);
        
        // Back Button 
        Button backButton = this.createButton("Back to main");
        backButton.setPosition(100, 300);
        backButton.setSize(100,50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuView(game));
            }
        });
        
        // Save Button
        Button saveButton = this.createButton("Save settings");
        saveButton.setPosition(100,200);
        saveButton.setSize(100,50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuView(game));
            }
        });

        stage.addActor(backButton);
        stage.addActor(select);
        stage.addActor(saveButton);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
