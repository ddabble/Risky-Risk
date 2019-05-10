package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.model.PlayerModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GameController {
    public static final GameController INSTANCE = new GameController();

    private Collection<GameObserver> observers = new ArrayList<>();

    private GameController() {}

    public int getPlayerColor(int playerID) {
        return PlayerModel.INSTANCE.getPlayerColor(playerID);
    }

    public Territory getSelectedTerritory() {
        return TroopModel.INSTANCE.getSelectedTerritory();
    }

    public void setSelectedTerritory(Territory territory) {
        TroopModel.INSTANCE.setSelectedTerritory(territory);
    }

    public void nextPhaseButtonClicked() {
        PhaseModel.INSTANCE.nextPhase();
        updatePhase();
    }

    public void boardClicked(Vector2 mapPos) {
        Territory territory = BoardModel.INSTANCE.getTerritory(mapPos);
        troopView.onSelectTerritory(territory);
        if (territory != null) {
            System.out.println(territory.name);

            // Update territory based on the phase we are in
            PhaseModel.INSTANCE.getPhase().territoryClicked(territory);

            troopView.onTerritoryChangeNumTroops(territory);
        } else
            System.out.println("None");
    }

    public void init() {
        TerritoryModel.init();

        PlayerModel.INSTANCE.init();
        BoardModel.INSTANCE.init();
        TroopModel.INSTANCE.init();

        updatePhase();
    }

    public void updatePhase() {
        String curPhase = PhaseModel.INSTANCE.getPhase().getName();
        String nextPhase = PhaseModel.INSTANCE.getPhase().next().getName();
        view.updatePhase(curPhase, nextPhase);
    }

    public void reset() {
        TroopModel.INSTANCE.reset();
        BoardModel.INSTANCE.reset();
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
