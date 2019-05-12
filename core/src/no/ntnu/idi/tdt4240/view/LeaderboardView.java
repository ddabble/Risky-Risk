package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.HashMap;

import no.ntnu.idi.tdt4240.observer.LeaderboardObserver;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;

public class LeaderboardView extends AbstractView implements LeaderboardObserver {
    private Stage stage;
    private Label leaderboardLabel;

    public LeaderboardView(){
        PhasePresenter.addObserver(this);
    }
    @Override
    public void create(HashMap<Integer, Integer> leaderboard) {
        super.create();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());


        // Actors
        leaderboardLabel = createLabel("");
        leaderboardLabel.setPosition(0, 400);

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
            result += "Player" + entry.getKey() + ":     " + entry.getValue() + "\n";
            //TODO: use StringBuilder.append() instead of +=
        }
        leaderboardLabel.setText(result);
    }

    @Override
    public void render() {
        // Draw and update
        //stage.act(); // Updates all actors
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
