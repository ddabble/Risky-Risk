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

    void removePhaseButtons();

    void updateRenderedVariables(String phase, int troopsToPlace);

    void onNextPlayer(int playerID, Color playerColor);

    void onNextPhase(String currentPhase, String nextPhase);

    void onSelectedTerritoriesChange(Territory start, Territory end);
}
