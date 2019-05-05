package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;

public class PhaseView extends AbstractView {

    public TextButton phaseButton;
    private Label phaseLabel;
    private Stage stage;

    public PhaseView(RiskyRisk game) {
        super(game);

        //For drawing and input handling
        this.stage = new Stage(new ScreenViewport());

        // Actors
        this.phaseLabel = this.createLabel("");
        phaseLabel.setPosition(0,200);
        this.phaseButton = this.createButton("");
        phaseButton.setWidth(100);

        stage.addActor(phaseLabel);
        stage.addActor(phaseButton);
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
