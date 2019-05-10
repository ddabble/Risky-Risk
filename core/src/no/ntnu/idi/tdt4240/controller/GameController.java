package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import no.ntnu.idi.tdt4240.data.Player;
import no.ntnu.idi.tdt4240.model.BattleModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GameController {
    public static final GameController INSTANCE = new GameController();

    private Collection<GameObserver> observers = new ArrayList<>();

    private GameController() {}

    public Player getPlayer(){ return playerModel; }

    public void init() {
        BoardController.INSTANCE.init();
        PhaseController.INSTANCE.init();
    }
    public void nextTurnButtonClicked(){
        phaseController.nextTurnButtonClicked();
        updatePhase();
        setSelectedTerritory(null);
        phaseView.onSelectedTerritoriesChange(null, null);


    }

    public void nextPhaseButtonClicked() {
        phaseController.clearRenderedButtons();
        if (phaseModel.getPhase().getName() == "Attack")
            model.getPlayerModel().cancelAttack();
        phaseModel.nextPhase();
        if (phaseModel.getPhase().getName() == "Fortify"){
            phaseView.addTurnButton();
        }
        updatePhase();
        setSelectedTerritory(null);
        phaseView.onSelectedTerritoriesChange(null, null);

    }

    public void cancelButtonClicked(){
        if (phaseModel.getPhase().getName() == "Attack"){
            playerModel.cancelAttack();
            phaseController.clearRenderedButtons();
            phaseView.onSelectedTerritoriesChange(null, null);
        }
        if (phaseModel.getPhase().getName() == "Fortify"){
            phaseController.cancelFortify();
            phaseView.onSelectedTerritoriesChange(null, null);
        }

    }

    public void attackButtonClicked(){
        phaseController.attackButtonClicked();
    }

    public void fortifyButtonClicked(){
        phaseController.fortifyButtonClicked();
    }

    @Override
    public void show() {
        phaseController.updateRenderedCurrentPlayer();
    }

    public void updatePhase() {
        if (phaseModel.getPhase().getName() == "Place") {
            phaseController.updateTroopsToPlace();
        }
    }

    public void reset() {
        BoardController.INSTANCE.reset();
    }

    /*
    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        view.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    */

    public static void addObserver(GameObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
