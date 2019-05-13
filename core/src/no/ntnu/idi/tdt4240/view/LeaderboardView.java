package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Map;

import no.ntnu.idi.tdt4240.observer.LeaderboardObserver;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;

public class LeaderboardView extends AbstractView implements LeaderboardObserver {
    private Stage stage;
    private Label leaderboardLabel;
    private ShapeRenderer shapeRenderer;

    public LeaderboardView() {
        PhasePresenter.addObserver(this);
    }

    @Override
    public void create(Map<Integer, Integer> leaderboard) {
        super.create();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();


        // Actors
        leaderboardLabel = createLeaderboardLabel("");
        leaderboardLabel.setAlignment(Align.topLeft);
        leaderboardLabel.setPosition(25, Gdx.graphics.getHeight() - 20);


        //the PhasePresenter has to update Leaderboard at the start;

        stage.addActor(leaderboardLabel);
    }

    /**
     * Define button for later use, but do not show them
     */

    public void addActor(Actor actor) {
        if (!stage.getActors().contains(actor, false))
            stage.addActor(actor);
    }

    public void removeActor(Actor actor) {
        if (stage.getActors().contains(actor, false))
            actor.remove();
    }


    /**
     * Updates the leaderboard with playerID and number of territories.
     *
     * @param leaderboard Map<Integer playerID, Integer numOfTerritories>
     */
    @Override
    public void updateLeaderboard(Map<Integer, Integer> leaderboard) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : leaderboard.entrySet()) {
            result.append("Player").append(entry.getKey()).append(":   ")
                  .append(entry.getValue()).append("\n");
        }
        leaderboardLabel.setText(result.toString());
    }

    @Override
    public void render() {
        // Draw and update
        //stage.act(); // Updates all actors
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //I'm using the Filled ShapeType, but remember you have three of them
        shapeRenderer.setColor(0, 0, 0, 0.2f);
        shapeRenderer.rect(10, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 2.2f - 10, Gdx.graphics.getWidth() / 7.7f, Gdx.graphics.getHeight() / 2.2f); //assuming you have created those x, y, width and height variables
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }

}
