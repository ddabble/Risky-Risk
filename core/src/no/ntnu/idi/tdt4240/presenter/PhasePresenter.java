package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ntnu.idi.tdt4240.model.data.Continent;
import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.model.AttackModel;
import no.ntnu.idi.tdt4240.model.BattleModel;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.model.TerritoryModel;
import no.ntnu.idi.tdt4240.model.TroopModel;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.observer.LeaderboardObserver;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;
import no.ntnu.idi.tdt4240.observer.TroopObserver;
import no.ntnu.idi.tdt4240.util.PhaseEnum;

public class PhasePresenter {
    public static final PhasePresenter INSTANCE = new PhasePresenter();

    private Collection<PhaseObserver> phaseObservers = new ArrayList<>();
    private Collection<TroopObserver> troopObservers = new ArrayList<>();
    private Collection<LeaderboardObserver> leaderboardObservers = new ArrayList<>();

    private Set<Integer> lostPlayers = new HashSet<>();

    private PhasePresenter() {}

    public void init(OrthographicCamera camera) {
        PhaseModel.INSTANCE.init();
        AttackModel.INSTANCE.init();

        for (PhaseObserver observer : phaseObservers) {
            observer.create(camera);
            updatePhase(observer);
        }

        for (LeaderboardObserver observer : leaderboardObservers) {
            Map<Integer, Integer> leaderboard = new HashMap<>();
            observer.create(leaderboard);
        }
        updateRenderedCurrentPlayer();

        // Update leaderboard
        onNumOfTerritoryChange(0, 0);
    }

    private void updatePhase(PhaseObserver observer) {
        PhaseEnum currentPhase = PhaseModel.INSTANCE.getPhase().getEnum();
        PhaseEnum nextPhase = PhaseModel.INSTANCE.getPhase().next().getEnum();
        observer.onNextPhase(currentPhase, nextPhase);

        if (PhaseModel.INSTANCE.getPhase().getEnum() == PhaseEnum.PLACE) {
            updateTroopsToPlace();
        }
    }

    public void onMapRenderingChanged() {
        for (TroopObserver observer : troopObservers)
            observer.onMapRenderingChanged();
        for (PhaseObserver observer : phaseObservers)
            observer.onMapRenderingChanged();
    }

    public void nextTurnButtonClicked() {
        if (BoardModel.INSTANCE.isOnlineMatch()) {
            nextTurnOnlineMatch();
        } else {
            nextTurnOfflineMatch();
        }
    }

    //handle giving the turn to the next player if its an online match
    private void nextTurnOnlineMatch() {
        TurnModel.INSTANCE.nextTurn();
        //since its an online match we need to update the state of the
        //match object on the server
        BoardModel.INSTANCE.updateAndSendMatchData();
        //for now just kick the player back to main menu
        for (PhaseObserver observer : phaseObservers) {
            observer.onWaitingForTurn();
        }
    }

    //pass turn to next player in an offline match
    private void nextTurnOfflineMatch() {
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
        phase.clearTerritorySelection();
        removePhaseButtons();
        TurnModel.INSTANCE.nextTurn();
        updateRenderedCurrentPlayer();
        for (PhaseObserver observer : phaseObservers)
            observer.removeTurnButton();

        PhaseModel.INSTANCE.nextPhase();
        updateTroopsToPlace();

        for (PhaseObserver observer : phaseObservers)
            updatePhase(observer);
        deselectTerritories();
    }

    public void nextPhaseButtonClicked() {
        removePhaseButtons();
        if (PhaseModel.INSTANCE.getPhase().getEnum() == PhaseEnum.ATTACK)
            AttackModel.INSTANCE.cancelAttack();
        PhaseModel.INSTANCE.nextPhase();
        deselectTerritories();
        if (PhaseModel.INSTANCE.getPhase().getEnum() == PhaseEnum.FORTIFY) {
            for (PhaseObserver observer : phaseObservers)
                observer.addTurnButton();
        }
        for (PhaseObserver observer : phaseObservers)
            updatePhase(observer);
    }

    private void deselectTerritories() {
        TroopModel.INSTANCE.onSelectTerritory(null);
        for (TroopObserver observer : troopObservers)
            observer.onSelectTerritory(null);

        for (PhaseObserver observer : phaseObservers)
            observer.onSelectedTerritoriesChange(null, null);
    }

    public void cancelButtonClicked() {
        if (PhaseModel.INSTANCE.getPhase().getEnum() == PhaseEnum.ATTACK) {
            AttackModel.INSTANCE.cancelAttack();
            removePhaseButtons();
        } else if (PhaseModel.INSTANCE.getPhase().getEnum() == PhaseEnum.FORTIFY) {
            PhasePresenter.INSTANCE.cancelFortify();
        }

        deselectTerritories();
    }

    private void updateRenderedCurrentPlayer() {
        // update rendered current player label and color
        for (PhaseObserver observer : phaseObservers) {
            observer.onNextPlayer(TurnModel.INSTANCE.getCurrentPlayerID(),
                                  MultiplayerModel.INSTANCE.getPlayerColor(TurnModel.INSTANCE.getCurrentPlayerID()));
        }
    }

    private void cancelFortify() {
        PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
        phase.clearTerritorySelection();
        removePhaseButtons();
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
                removePhaseButtons();
                deselectTerritories();
            }
        }
    }

    private void removePhaseButtons() {
        for (PhaseObserver observer : phaseObservers) {
            observer.removePhaseButtons();
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
            TurnModel.INSTANCE.nextTurn();
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

    public void attackButtonClicked() {
        int defenderID = AttackModel.INSTANCE.getToTerritory().getOwnerID();
        int[] winner = BattleModel.fight(AttackModel.INSTANCE.getFromTerritory().getOwnerID(),
                                         AttackModel.INSTANCE.getToTerritory().getOwnerID(),
                                         AttackModel.INSTANCE.getFromTerritory().getNumTroops() - 1,
                                         AttackModel.INSTANCE.getToTerritory().getNumTroops());

        // update phase observers
        for (PhaseObserver observer : phaseObservers) {
            observer.removePhaseButtons();
        }
        // update territory models
        AttackModel.INSTANCE.getToTerritory().setOwnerID(winner[0]);
        AttackModel.INSTANCE.getToTerritory().setNumTroops(winner[1]);
        AttackModel.INSTANCE.getFromTerritory().setNumTroops(1);

        // update leaderboard
        // if the attacker won the fight.
        if (AttackModel.INSTANCE.getToTerritory().getOwnerID() == AttackModel.INSTANCE.getFromTerritory().getOwnerID()) {
            onNumOfTerritoryChange(winner[0], 1);
            onNumOfTerritoryChange(defenderID, -1);
        }

        // update the troop observers
        for (TroopObserver observer : troopObservers) {
            observer.onTerritoryChangeNumTroops(AttackModel.INSTANCE.getFromTerritory());
            observer.onTerritoryChangeNumTroops(AttackModel.INSTANCE.getToTerritory());
        }
        BoardPresenter.INSTANCE.onTerritoryChangedOwner(AttackModel.INSTANCE.getToTerritory());
        //Clears the attack HashMap after the attack has gone through.
        AttackModel.INSTANCE.cancelAttack();
        deselectTerritories();
        System.out.println(" - Player" + winner[0] + " won this fight. - ");
        checkGameOver();
    }

    public void exitToMainMenuButtonClicked() {
        // TODO: add message view to ask the player "Are you sure you want to exit?" and "All progress will be lost"
        BoardModel.INSTANCE.getClient().setMatchNotActive(); //extremely hacky, board should not be handing out the client, instead the
        //client should be its own singleton model
        GamePresenter.INSTANCE.exitToMainMenu();
    }

    /**
     * Exits the game if game is over
     */
    private void checkGameOver() {
        // Check if game is over (one player owns all territories)
        if (GamePresenter.INSTANCE.isGameOver()) {
            GamePresenter.INSTANCE.exitToWinScreen();
        }
    }

    /**
     * Updates leaderboard according to the state of the game
     * This approach updates only the player of the territories changed.
     * This affects the performance less than counting the territory map as a whole
     *
     * @param diff how many territories this player has gained or lost
     */
    private void onNumOfTerritoryChange(int playerID, int diff) {
        //get players
        Map<Integer, Integer> leaderboard = MultiplayerModel.INSTANCE.getLeaderboard();
        int currentNumOfTerritories = leaderboard.get(playerID);
        leaderboard.put(playerID, currentNumOfTerritories + diff);
        MultiplayerModel.INSTANCE.setLeaderboard(leaderboard);

        for (LeaderboardObserver observer : leaderboardObservers)
            observer.updateLeaderboard(leaderboard);
    }

    public void onTerritoryClicked(Territory territory) {
        //PhaseModel.INSTANCE.getPhase().territoryClicked(territory); //update the model
        // update the view
        switch (PhaseModel.INSTANCE.getPhase().getEnum()) {
            case PLACE:
                if (AttackModel.INSTANCE.getTroopsToPlace() > 0 && territory.getOwnerID() == TurnModel.INSTANCE.getCurrentPlayerID()) {
                    PhaseModel.INSTANCE.getPhase().territoryClicked(territory);
                    AttackModel.INSTANCE.setTroopsToPlace(AttackModel.INSTANCE.getTroopsToPlace() - 1);
                    for (TroopObserver observer : troopObservers)
                        observer.onTerritoryChangeNumTroops(territory);
                    for (PhaseObserver observer : phaseObservers)
                        observer.updateRenderedVariables(PhaseModel.INSTANCE.getPhase().getEnum().toString(), AttackModel.INSTANCE.getTroopsToPlace());
                } else {
                    System.out.println("No troops left to place");
                }
                break;

            case ATTACK:
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
                break;

            case FORTIFY:
                PhaseModel.FortifyPhase phase = (PhaseModel.FortifyPhase)PhaseModel.INSTANCE.getPhase();
                phase.territoryClicked(territory, TurnModel.INSTANCE.getCurrentPlayerID());
                //update UI
                if (phase.getSelectedFrom() != null && phase.getSelectedTo() != null) {
                    for (PhaseObserver observer : phaseObservers) {
                        observer.addFortifyButton();
                        observer.addCancelButton();
                    }
                }
                for (PhaseObserver observer : phaseObservers)
                    observer.onSelectedTerritoriesChange(phase.getSelectedFrom(), phase.getSelectedTo());
                break;
        }
    }

    public void reset() {
        AttackModel.INSTANCE.reset();
    }

    public static void addObserver(PhaseObserver observer) {
        INSTANCE.phaseObservers.add(observer);
    }

    public static void addObserver(TroopObserver observer) {
        INSTANCE.troopObservers.add(observer);
    }

    public static void addObserver(LeaderboardObserver observer) {
        INSTANCE.leaderboardObservers.add(observer);
    }
}
