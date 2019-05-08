package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;

public class PhaseView extends AbstractView {
    private TextButton phaseButton;
    private Label phaseLabel;
    private Stage stage;

    public PhaseView(RiskyRisk game) {
        super(game);
    }

    public void updatePhase(String curPhase, String nextPhase) {
        phaseLabel.setText("Current Phase: " + curPhase);
        phaseButton.setText(nextPhase);
    }

    @Override
    public void show() {
        super.show();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());

        // Actors
        phaseLabel = createLabel("");
        phaseLabel.setPosition(0, 200);
        phaseButton = createButton("");
        phaseButton.setWidth(100);

        stage.addActor(phaseLabel);
        stage.addActor(phaseButton);
    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw and update
        stage.act(delta); // Updates all actors
        stage.draw();
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
        stage.dispose();
        super.hide();
    }

    @Override
    public void dispose() {

    }
}
