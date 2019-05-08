package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.PhaseController;
import no.ntnu.idi.tdt4240.data.Territory;

public class PhaseView extends AbstractView {
    private TextButton phaseButton;
    private Label phaseLabel;
    private Stage stage;
    private static OrthographicCamera camera;
    private PhaseController controller;


    private Vector2 lineFrom;
    private Vector2 lineTo;
    private boolean shouldDrawArrow = false;

    public PhaseView(RiskyRisk game, OrthographicCamera camera) {
        super(game);
        this.camera = camera;
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

        if(shouldDrawArrow)
            drawLine(lineFrom,lineTo);

    }

    @Override
    public void hide() {
        stage.dispose();
        super.hide();
    }

    public void onSelectedTerritoriesChange(Territory start, Territory end) {
        if (start != null && end != null){
            lineFrom = start.getTroopCircleVector();
            lineTo = end.getTroopCircleVector();
            shouldDrawArrow = true;
        } else {
            shouldDrawArrow = false;
        }
    }



    public void onMapMove() {
    }

    private void drawLine(Vector2 start, Vector2 end)
    {
        ShapeRenderer debugRenderer = new ShapeRenderer();
        Gdx.gl.glLineWidth(4);
        debugRenderer.setProjectionMatrix(camera.combined);
        debugRenderer.begin(ShapeRenderer.ShapeType.Line);
        debugRenderer.setColor(Color.BLACK);
        debugRenderer.line(start, end);
        debugRenderer.end();
        Gdx.gl.glLineWidth(1); //set back to default
    }
}
