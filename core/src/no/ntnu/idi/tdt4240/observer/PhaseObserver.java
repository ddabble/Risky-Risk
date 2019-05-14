package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.PhaseEnum;

public interface PhaseObserver {
    Stage getStage();

    void create(OrthographicCamera camera);

    void addFortifyButton();

    void addCancelButton();

    void addAttackButton();

    void addTurnButton();

    void removeTurnButton();

    void removePhaseButtons();

    void updateRenderedVariables(String phase, int troopsToPlace);

    void onMapRenderingChanged();

    void onNextPlayer(int playerID, Color playerColor);

    void onNextPhase(PhaseEnum currentPhase, PhaseEnum nextPhase);

    void onSelectedTerritoriesChange(Territory start, Territory end);

    //called when the match state has been sent to the server and
    //the local player has to wait until its their turn
    void onWaitingForTurn();
}
