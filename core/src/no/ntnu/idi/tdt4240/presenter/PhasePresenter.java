package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ntnu.idi.tdt4240.data.Continent;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.AttackModel;
import no.ntnu.idi.tdt4240.model.BattleModel;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;
import no.ntnu.idi.tdt4240.observer.TroopObserver;

public class PhasePresenter {
    public static final PhasePresenter INSTANCE = new PhasePresenter();

    private Collection<PhaseObserver> phaseObservers = new ArrayList<>();
    private Collection<TroopObserver> troopObservers = new ArrayList<>();

    private PhasePresenter() {}

    public void init() {
        AttackModel.INSTANCE.init();

        for (PhaseObserver observer : phaseObservers) {
            observer.create();
            updatePhase(observer);
        }
        updateRenderedCurrentPlayer();
    }

    private void updatePhase(PhaseObserver observer) {
        String currentPhase = PhaseModel.INSTANCE.getPhase().getName();
        String nextPhase = PhaseModel.INSTANCE.getPhase().next().getName();
        observer.updatePhase(currentPhase, nextPhase);

        if (PhaseModel.INSTANCE.getPhase().getName().equals("Place")) {
            updateTroopsToPlace();
        }
    }

    public void boardClicked(Vector2 touchWorldPos) { // maybe make interface for this

    }

    //this is where we need to update the match object in board and send it
    //through GPGS if we are playing online multiplayer
    public void nextTurnButtonClicked() {
        if(BoardModel.INSTANCE.isOnlineMatch()) {
            nextTurnOnlineMatch();
        } else {
            nextTurnOfflineMatch();
        }
    }

    //handle giving the turn to the next player if its an online match
    private void nextTurnOnlineMatch() {
        //since its an online match we need to update the state of the
        //match object on the server
        BoardModel.INSTANCE.updateAndSendMatchData();
    }

    //pass turn to next player in an offline match
    private void nextTurnOfflineMatch() {
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
        phase.clearTerritorySelection();
        clearRenderedButtons();
        TurnModel.INSTANCE.takeTurn();
        updateRenderedCurrentPlayer();
        for (PhaseObserver observer : phaseObservers)
            observer.removeTurnButton();

        PhaseModel.INSTANCE.nextPhase();
        updateTroopsToPlace();

        for (PhaseObserver observer : phaseObservers)
            updatePhase(observer);
        TroopModel.INSTANCE.onSelectTerritory(null);
        for (PhaseObserver observer : phaseObservers)
            observer.onSelectedTerritoriesChange(null, null);
    }

    public void nextPhaseButtonClicked() {
        PhasePresenter.INSTANCE.clearRenderedButtons();
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Attack"))
            AttackModel.INSTANCE.cancelAttack();
        PhaseModel.INSTANCE.nextPhase();
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Fortify")) {
            for (PhaseObserver observer : phaseObservers)
                observer.addTurnButton();
        }
        for (PhaseObserver observer : phaseObservers)
            updatePhase(observer);
        TroopModel.INSTANCE.onSelectTerritory(null);
        for (PhaseObserver observer : phaseObservers)
            observer.onSelectedTerritoriesChange(null, null);
    }

    public void cancelButtonClicked() {
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Attack")) {
            AttackModel.INSTANCE.cancelAttack();
            PhasePresenter.INSTANCE.clearRenderedButtons();
            for (PhaseObserver observer : phaseObservers)
                observer.onSelectedTerritoriesChange(null, null);
        }
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Fortify")) {
            PhasePresenter.INSTANCE.cancelFortify();
            for (PhaseObserver observer : phaseObservers)
                observer.onSelectedTerritoriesChange(null, null);
        }
    }

    private void updateRenderedCurrentPlayer() {
        for (PhaseObserver observer : phaseObservers) {
            observer.updateRenderedCurrentPlayer(TurnModel.INSTANCE.getCurrentPlayerID(),
                                                 MultiplayerModel.INSTANCE.getPlayerColor(TurnModel.INSTANCE.getCurrentPlayerID()));
        }
    }

    private void cancelFortify() {
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
        phase.clearTerritorySelection();
        clearRenderedButtons();
    }

    public void fortifyButtonClicked() {
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
        if (phase.getSelectedFrom().getNumTroops() > 1) {
            phase.getSelectedFrom().setNumTroops(phase.getSelectedFrom().getNumTroops() - 1);
            phase.getSelectedTo().setNumTroops(phase.getSelectedTo().getNumTroops() + 1);
            for (TroopObserver observer : troopObservers) {
                observer.onTerritoryChangeNumTroops(phase.getSelectedFrom());
                observer.onTerritoryChangeNumTroops(phase.getSelectedTo());
            }
            if (phase.getSelectedFrom().getNumTroops() == 1) {
                phase.clearTerritorySelection();
                clearRenderedButtons();
                for (PhaseObserver observer : phaseObservers)
                    observer.onSelectedTerritoriesChange(null, null);
            }
        }

    }

    private void updateTroopsToPlace() {
        List<Territory> territories = TerritoryModel.getTerritoryMap().getAllTerritories();
        List<Territory> possibleContinent = new ArrayList<>();
        int territoriesOwned = 0;
        for (Territory territory : territories) {
            // Temporary ID Check
            if (territory.getOwnerID() == TurnModel.INSTANCE.getCurrentPlayerID()) {
                territoriesOwned++;
                possibleContinent.add(territory);
            }
        }
        if (territoriesOwned == 0) {
            TurnModel.INSTANCE.takeTurn();
            updateTroopsToPlace();
            updateRenderedCurrentPlayer();
        } else {
            boolean hasContinent;
            int extraTroops = 0;
            for (Continent continent : TerritoryModel.getTerritoryMap().getAllContinents()) {
                hasContinent = true;
                for (Territory territory : continent.getTerritories()) {
                    if (!possibleContinent.contains(territory))
                        hasContinent = false;
                }
                if (hasContinent)
                    extraTroops = continent.getBonusTroops();
            }
            AttackModel.INSTANCE.setTroopsToPlace(Math.max((int)Math.ceil(territoriesOwned / 3f), 3) + extraTroops);
            for (PhaseObserver observer : phaseObservers)
                observer.updateRenderedVariables("Place", AttackModel.INSTANCE.getTroopsToPlace());
        }
    }

    private void clearRenderedButtons() {
        for (PhaseObserver observer : phaseObservers) {
            observer.removeAttackButton();
            observer.removeFortifyButton();
            observer.removeCancelButton();
        }
    }

    public void attackButtonClicked() {
        int[] winner = BattleModel.fight(AttackModel.INSTANCE.getFromTerritory().getOwnerID(),
                                         AttackModel.INSTANCE.getToTerritory().getOwnerID(),
                                         AttackModel.INSTANCE.getFromTerritory().getNumTroops() - 1,
                                         AttackModel.INSTANCE.getToTerritory().getNumTroops());
        clearRenderedButtons();
        AttackModel.INSTANCE.getToTerritory().setOwnerID(winner[0]);
        AttackModel.INSTANCE.getToTerritory().setNumTroops(winner[1]);
        AttackModel.INSTANCE.getFromTerritory().setNumTroops(1);
        for (TroopObserver observer : troopObservers) {
            observer.onTerritoryChangeNumTroops(AttackModel.INSTANCE.getFromTerritory());
            observer.onTerritoryChangeNumTroops(AttackModel.INSTANCE.getToTerritory());
        }
        BoardPresenter.INSTANCE.onTerritoryChangedOwner(AttackModel.INSTANCE.getToTerritory());
        //Clears the attack HashMap after the attack has gone through.
        AttackModel.INSTANCE.cancelAttack();
        for (PhaseObserver observer : phaseObservers)
            observer.onSelectedTerritoriesChange(null, null);
        for (TroopObserver observer : troopObservers)
            observer.onSelectTerritory(null);
        System.out.println(winner.toString());
    }

    public void onTerritoryClicked(Territory territory) {
        //PhaseModel.INSTANCE.getPhase().territoryClicked(territory); //update the model
        // update the view
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Place")) {
            if (AttackModel.INSTANCE.getTroopsToPlace() > 0 && territory.getOwnerID() == TurnModel.INSTANCE.getCurrentPlayerID()) {
                PhaseModel.INSTANCE.getPhase().territoryClicked(territory);
                AttackModel.INSTANCE.setTroopsToPlace(AttackModel.INSTANCE.getTroopsToPlace() - 1);
                for (TroopObserver observer : troopObservers)
                    observer.onTerritoryChangeNumTroops(territory);
                for (PhaseObserver observer : phaseObservers)
                    observer.updateRenderedVariables(PhaseModel.INSTANCE.getPhase().getName(), AttackModel.INSTANCE.getTroopsToPlace());
            } else {
                System.out.println("No troops left to place");
            }
        }
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Attack")) {
            if (territory.getOwnerID() == TurnModel.INSTANCE.getCurrentPlayerID() && territory.getNumTroops() > 1) {
                if (AttackModel.INSTANCE.getAttack() == null || AttackModel.INSTANCE.getAttack().size() == 0) {
                    AttackModel.INSTANCE.setAttackFrom(territory);
                    for (PhaseObserver observer : phaseObservers)
                        observer.addCancelButton();
                }
            }
            if (AttackModel.INSTANCE.getAttack() != null) {
                if (AttackModel.INSTANCE.getAttack().size() == 1 && territory.getOwnerID() != TurnModel.INSTANCE.getCurrentPlayerID()) {
                    if (AttackModel.INSTANCE.getFromTerritory().getNeighbors().contains(territory)) {
                        AttackModel.INSTANCE.setAttackTo(territory);
                        for (PhaseObserver observer : phaseObservers) {
                            observer.onSelectedTerritoriesChange(AttackModel.INSTANCE.getFromTerritory(), AttackModel.INSTANCE.getToTerritory());
                            observer.addAttackButton();
                        }
                    }
                } else if (AttackModel.INSTANCE.getAttack().size() == 2 && territory.getOwnerID() != TurnModel.INSTANCE.getCurrentPlayerID()) {
                    if (AttackModel.INSTANCE.getFromTerritory().getNeighbors().contains(territory)) {
                        AttackModel.INSTANCE.setAttackTo(territory);
                        for (PhaseObserver observer : phaseObservers)
                            observer.onSelectedTerritoriesChange(AttackModel.INSTANCE.getFromTerritory(), AttackModel.INSTANCE.getToTerritory());
                    }
                }
            }
        }
        if (PhaseModel.INSTANCE.getPhase().getName().equals("Fortify")) {
            PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
            phase.territoryClicked(territory, TurnModel.INSTANCE.getCurrentPlayerID());
            //phase.getSelectedFrom();
            //update UI
            System.out.println("Draw line");
            if (phase.getSelectedFrom() != null && phase.getSelectedTo() != null) {
                for (PhaseObserver observer : phaseObservers) {
                    observer.addFortifyButton();
                    observer.addCancelButton();
                }
            }
            for (PhaseObserver observer : phaseObservers)
                observer.onSelectedTerritoriesChange(phase.getSelectedFrom(), phase.getSelectedTo());
        }
    }

    public static void addObserver(PhaseObserver observer) {
        INSTANCE.phaseObservers.add(observer);
    }

    public static void addObserver(TroopObserver observer) {
        INSTANCE.troopObservers.add(observer);
    }
}
