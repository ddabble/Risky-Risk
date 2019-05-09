package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import no.ntnu.idi.tdt4240.data.Player;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BattleModel;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.FortifyModel;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.view.BoardView;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.PhaseView;
import no.ntnu.idi.tdt4240.view.TroopView;

public class PhaseController {
    private GameModel gameModel;
    private GameView gameView;
    private final BoardModel boardModel;
    private final TroopModel troopModel;
    private final PhaseModel phaseModel;
    private final Player playerModel;
    private final TurnModel turnModel;
    private final PhaseView phaseView;
    private final BoardView boardView;
    private final TroopView troopView;

    public PhaseController(GameModel gameModel, GameView gameView){
        this.gameModel = gameModel;
        this.gameView = gameView;
        boardModel = gameModel.getBoardModel();
        troopModel = gameModel.getTroopModel();
        phaseModel = gameModel.getPhaseModel();
        playerModel = gameModel.getPlayerModel();
        turnModel = gameModel.getTurnModel();

        phaseView = gameView.getPhaseView();
        boardView = gameView.getBoardView();
        troopView = gameView.getTroopView();
    }

    public void boardClicked(Vector2 touchWorldPos){ // maybe make interface for this

    }

    public void nextTurnButtonClicked(){
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)phaseModel.getPhase();
        phase.clearTerritorySelection();
        clearRenderedButtons();
        turnModel.takeTurn();
        phaseView.updateRenderedCurrentPlayer
                (turnModel.getCurrentPlayerID(), getPlayerRGBAColor(turnModel.getCurrentPlayerID()));
        phaseView.removeTurnButton();
        phaseModel.nextPhase();

    }

    public void updateRenderedCurrentPlayer(){
        phaseView.updateRenderedCurrentPlayer(turnModel.getCurrentPlayerID(), getPlayerRGBAColor(turnModel.getCurrentPlayerID()));
    }

    public void cancelFortify(){
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)phaseModel.getPhase();
        phase.clearTerritorySelection();
        clearRenderedButtons();
    }

    public void fortifyButtonClicked(){
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)phaseModel.getPhase();
        if (phase.getSelectedFrom().getNumTroops() > 1){
            phase.getSelectedFrom().setNumTroops(phase.getSelectedFrom().getNumTroops() - 1);
            phase.getSelectedTo().setNumTroops(phase.getSelectedTo().getNumTroops() + 1);
            troopView.onTerritoryChangeNumTroops(phase.getSelectedFrom());
            troopView.onTerritoryChangeNumTroops(phase.getSelectedTo());
            if (phase.getSelectedFrom().getNumTroops() == 1){
                phase.clearTerritorySelection();
                clearRenderedButtons();
                phaseView.onSelectedTerritoriesChange(null, null);
            }
        }

    }

    public void updateTroopsToPlace(){
        List<Territory> territories = TerritoryModel.getInstance().TERRITORY_MAP.getAllTerritories();
        int territoriesOwned = 0;
        for (Territory territory : territories)
            // Temporary ID Check
            if (territory.getOwnerID() ==  turnModel.getCurrentPlayerID())
                territoriesOwned++;
        if (territoriesOwned == 0){
            turnModel.takeTurn();
            updateTroopsToPlace();

        }
        else{
            playerModel.setTroopsToPlace(territoriesOwned);
            phaseView.updateRenderedVariables("Place");
        }
    }

    public Color getPlayerRGBAColor(int playerID){
        return gameModel.getMultiplayerModel().getPlayerRGBAColor(playerID);
    }

    public void clearRenderedButtons(){
        phaseView.removeAttackButton();
        phaseView.removeFortifyButton();
        phaseView.removeCancelButton();
    }

    public void attackButtonClicked(){
        int[] winner = BattleModel.fight(playerModel.getFromTerritory().getOwnerID(),
                playerModel.getToTerritory().getOwnerID(),
                playerModel.getFromTerritory().getNumTroops() - 1,
                playerModel.getToTerritory().getNumTroops());
        clearRenderedButtons();
        playerModel.getToTerritory().setOwnerID(winner[0]);
        playerModel.getToTerritory().setNumTroops(winner[1]);
        playerModel.getFromTerritory().setNumTroops(1);
        troopView.onTerritoryChangeNumTroops(playerModel.getFromTerritory());
        troopView.onTerritoryChangeNumTroops(playerModel.getToTerritory());
        boardView.initColorLookupArray(TerritoryModel.getInstance().TERRITORY_MAP);
        //Clears the attack HashMap after the attack has gone through.
        playerModel.cancelAttack();
        phaseView.onSelectedTerritoriesChange(null, null);
        troopView.onSelectTerritory(null);
        System.out.println(winner.toString());
    }
    public void territoryClicked(Territory territory){ // called GameController
        //phaseModel.getPhase().territoryClicked(territory); //update the model
        // update the view
        if (phaseModel.getPhase().getName() == "Place") {
            if (playerModel.getTroopsToPlace() > 0 && territory.getOwnerID() == turnModel.getCurrentPlayerID()) {
                phaseModel.getPhase().territoryClicked(territory);
                playerModel.setTroopsToPlace(playerModel.getTroopsToPlace() - 1);
                troopView.onTerritoryChangeNumTroops(territory);
                phaseView.updateRenderedVariables(phaseModel.getPhase().getName());
            } else {
                System.out.println("No troops left to place");
            }
        }
        if (phaseModel.getPhase().getName() == "Attack") {
            if (territory.getOwnerID() == turnModel.getCurrentPlayerID() && territory.getNumTroops() > 1) {
                if (playerModel.getAttack() == null || playerModel.getAttack().size() == 0) {
                    playerModel.setAttackFrom(territory);
                    phaseView.addCancelButton();
                }
            }
            if (playerModel.getAttack() != null) {
                if (playerModel.getAttack().size() == 1 && territory.getOwnerID() != turnModel.getCurrentPlayerID()){
                    if (playerModel.getFromTerritory().getNeighbors().contains(territory)) {
                        playerModel.setAttackTo(territory);
                        phaseView.onSelectedTerritoriesChange(playerModel.getFromTerritory(), playerModel.getToTerritory());
                        phaseView.addAttackButton();
                    }
                }
                else if (playerModel.getAttack().size() == 2 && territory.getOwnerID() != turnModel.getCurrentPlayerID()){
                    if (playerModel.getFromTerritory().getNeighbors().contains(territory)) {
                        playerModel.setAttackTo(territory);
                        phaseView.onSelectedTerritoriesChange(playerModel.getFromTerritory(), playerModel.getToTerritory());
                    }
                }
            }
        }
        if (phaseModel.getPhase().getName() == "Fortify") {
            PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)phaseModel.getPhase();
            phase.territoryClicked(territory, turnModel.getCurrentPlayerID());
            //phase.getSelectedFrom();
            //update UI
            System.out.println("Draw line");
            if (phase.getSelectedFrom() != null && phase.getSelectedTo() != null){
                phaseView.addFortifyButton();
                phaseView.addCancelButton();
            }
            phaseView.onSelectedTerritoriesChange(phase.getSelectedFrom(), phase.getSelectedTo());
        }
    }
}
