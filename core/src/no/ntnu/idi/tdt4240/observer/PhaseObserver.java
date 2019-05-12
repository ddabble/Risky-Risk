package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.ntnu.idi.tdt4240.data.Territory;

public interface PhaseObserver {
    Stage getStage();

    void create();

    void addFortifyButton();

    void addCancelButton();

    void addAttackButton();

    void addTurnButton();

    void removeTurnButton();

    void removeFortifyButton();

    void removeCancelButton();

    void removeAttackButton();

    void updateRenderedVariables(String phase, int troopsToPlace);

    void updateRenderedCurrentPlayer(int playerID, Color playerColor);

    void updatePhase(String currentPhase, String nextPhase);

    void onSelectedTerritoriesChange(Territory start, Territory end);

    //called when the match state has been sent to the server and
    //the local player has to wait until its their turn
    void onWaitingForTurn();
}
