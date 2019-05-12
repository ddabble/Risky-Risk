package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

import no.ntnu.idi.tdt4240.observer.LeaderboardObserver;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;

public class LeaderboardView extends AbstractView implements LeaderboardObserver {
    private Stage stage;
    private Label leaderboardLabel;
    private ShapeRenderer shapeRenderer;

    public LeaderboardView(){
        PhasePresenter.addObserver(this);
    }
    @Override
    public void create(HashMap<Integer, Integer> leaderboard) {
        super.create();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();


        // Actors
        leaderboardLabel = createLeaderboardLabel("");
        leaderboardLabel.setAlignment(Align.topLeft);
        leaderboardLabel.setPosition(25, Gdx.graphics.getHeight() - 20);


        //the phasepresenter has to update Leaderboard at the start;

        stage.addActor(leaderboardLabel);
    }

    /**
     * Define button for later use, but do not show them
     */

    public void addActor(Actor actor){
        if (!stage.getActors().contains(actor, false))
            stage.addActor(actor);
    }
    public void removeActor(Actor actor){
        if (stage.getActors().contains(actor, false))
            actor.remove();
    }


    /**
     * Updates the leaderboard with playerID and number of territories.
     * @param leaderboard Map<Integer playerID, Integer numOfTerritories>
     */
    @Override
    public void updateLeaderboard(HashMap<Integer, Integer> leaderboard) {
        String result = "";
        for (HashMap.Entry<Integer, Integer> entry : leaderboard.entrySet()) {
            result += "Player" + entry.getKey() + ":   " + entry.getValue() + "\n";
            //TODO: use StringBuilder.append() instead of +=
        }
        leaderboardLabel.setText(result);
    }

    @Override
    public void render() {
        // Draw and update
        //stage.act(); // Updates all actors
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); //I'm using the Filled ShapeType, but remember you have three of them
        shapeRenderer.setColor(0, 0, 0, 0.2f);
        shapeRenderer.rect(10,Gdx.graphics.getHeight()-Gdx.graphics.getHeight()/2.2f-10,Gdx.graphics.getWidth()/7.7f,Gdx.graphics.getHeight()/2.2f); //assuming you have created those x, y, width and height variables
        shapeRenderer.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }

}
