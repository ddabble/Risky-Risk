package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.controller.PhaseController;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;

public class PhaseView extends AbstractView implements PhaseObserver {
    private TextButton phaseButton;
    private Label phaseLabel;
    private Stage stage;

    public PhaseView() {
        PhaseController.addObserver(this);
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void create() {
        super.create();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());

        // Actors
        phaseLabel = createLabel("");
        phaseLabel.setPosition(0, 200);
        phaseButton = createButton("");
        phaseButton.setWidth(100);
        phaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhaseController.INSTANCE.nextPhaseButtonClicked();
            }
        });

        stage.addActor(phaseLabel);
        stage.addActor(phaseButton);
    }

    @Override
    public void updatePhase(String curPhase, String nextPhase) {
        phaseLabel.setText("Current Phase: " + curPhase);
        phaseButton.setText(nextPhase);
    }

    @Override
    public void render() {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw and update
        stage.act(); // Updates all actors
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
