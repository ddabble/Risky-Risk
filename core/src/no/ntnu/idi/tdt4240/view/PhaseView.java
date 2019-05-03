package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

import no.ntnu.idi.tdt4240.controller.PhaseController;
import no.ntnu.idi.tdt4240.controller.PhaseViewer;
import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.model.PhaseModel;

public class PhaseView extends AbstractView implements PhaseViewer {

    public TextButton phaseButton;
    private Label phaseLabel;
    private Stage stage;
    public final PhaseController phaseController;
    private HashMap<Integer, TextButton> territories = new HashMap<Integer, TextButton>();

    public PhaseView(RiskyRisk game) {
        super(game);
        PhaseModel phaseModel = new PhaseModel();
        //For drawing and input handling
        this.stage = new Stage(new ScreenViewport());
        //Gdx.input.setInputProcessor(stage);

        // Actors
        this.phaseLabel = this.createLabel("");
        phaseLabel.setPosition(0,200);
        this.phaseButton = this.createButton("");
        phaseButton.setWidth(100);

        //Controller, one or several
        /* NOTE: phaseController needs to be initialized before anything that needs
         * to be filled by model through the controller, before anything that updates the model
         * through the controller
        */

        this.phaseController = new PhaseController(this);



        //Pseudomap
        Actor map = new Actor();

        // TODO: click under

        stage.addActor(phaseLabel);
        stage.addActor(phaseButton);

        // get intial values phaseController.updatePhase();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Draw and update
        this.stage.act(delta); //Updates all actors
        this.stage.draw();
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
        stage.dispose();
    }

    public void updatePhase(String curPhase, String nextPhase){
        this.phaseLabel.setText("Current Phase: " + curPhase);
        this.phaseButton.setText(nextPhase);
    }
}
