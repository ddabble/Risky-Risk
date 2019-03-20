package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.Controllers.Controller;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class SettingsView extends AbstractView {
    private final Controller controller;
    private Stage stage;
    private RiskyRisk game;
    Texture background;
    private Label label;

    public SettingsView(RiskyRisk game) {
        super(game);
        this.game = game;
        background = new Texture("settings.png");
        this.controller = new Controller(this);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Select Box
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

        // Label Button
        Button labelButton = this.createButton("Change label with settings");
        saveButton.setPosition(100,100);
        saveButton.setSize(100,50);

        labelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (controller.getSetting1().equals("foo")) {
                    controller.setSetting1("bar");
                } else {
                    controller.setSetting1("foo");
                }
            }
        });

        //select.setSelected(controller.getSetting1());

        // Label
        this.label = this.createLabel(controller.getSetting1());
        label.setPosition(200,100);

        stage.addActor(backButton);
        stage.addActor(select);
        stage.addActor(saveButton);
        stage.addActor(labelButton);
        stage.addActor(label);


    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        game.batch.begin();
        game.font.draw(game.batch, "Welcome to the SettingsView!!! ", 100, 150);
        game.batch.end();
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
