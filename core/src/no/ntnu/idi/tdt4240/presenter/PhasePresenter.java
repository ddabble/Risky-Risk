package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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

    public static void init(OrthographicCamera camera) {
        INSTANCE._init(camera);
    }

    private void _init(OrthographicCamera camera) {
        PhaseModel.init();
        AttackModel.init();

        for (PhaseObserver observer : phaseObservers) {
            observer.create(camera);
            updatePhase(observer);
        }

        for (LeaderboardObserver observer : leaderboardObservers) {
            observer.create();
        }

        updateRenderedCurrentPlayer();
        updateLeaderboard();
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
        final Territory fromTerritory = phase.getSelectedFrom();
        final Territory toTerritory = phase.getSelectedTo();

        if (fromTerritory.getNumTroops() > 1) {
            fromTerritory.setNumTroops(fromTerritory.getNumTroops() - 1);
            toTerritory.setNumTroops(toTerritory.getNumTroops() + 1);
            for (TroopObserver observer : troopObservers) {
                observer.onTerritoryChangeNumTroops(fromTerritory);
                observer.onTerritoryChangeNumTroops(toTerritory);
            }
            if (fromTerritory.getNumTroops() == 1) {
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
        int currentPlayerID = TurnModel.INSTANCE.getCurrentPlayerID();
        Set<Territory> possibleContinent = MultiplayerModel.INSTANCE.getTerritoriesOwnedByPlayer(currentPlayerID);

        boolean hasContinent;
        int extraTroops = 0;
        for (Continent continent : TerritoryModel.getTerritoryMap().getAllContinents()) {
            hasContinent = true;
            for (Territory territory : continent.getTerritories()) {
                if (!possibleContinent.contains(territory))
                    hasContinent = false;
            }
            if (hasContinent)
                extraTroops += continent.getBonusTroops();
        }

        int numTerritoriesOwned = possibleContinent.size();
        AttackModel.INSTANCE.setTroopsToPlace(Math.max((int)Math.ceil(numTerritoriesOwned / 3f), 3) + extraTroops);

        for (PhaseObserver observer : phaseObservers)
            observer.updateRenderedVariables("Place", AttackModel.INSTANCE.getTroopsToPlace());
    }

    public void attackButtonClicked() {
        final Territory fromTerritory = AttackModel.INSTANCE.getFromTerritory();
        final Territory toTerritory = AttackModel.INSTANCE.getToTerritory();

        int defenderID = toTerritory.getOwnerID();
        int[] winner = BattleModel.fight(fromTerritory.getOwnerID(),
                                         toTerritory.getOwnerID(),
                                         fromTerritory.getNumTroops() - 1,
                                         toTerritory.getNumTroops());

        // update phase observers
        for (PhaseObserver observer : phaseObservers) {
            observer.removePhaseButtons();
        }
        // update territory models
        toTerritory.setOwnerID(winner[0]);
        toTerritory.setNumTroops(winner[1]);
        fromTerritory.setNumTroops(1);

        // update leaderboard
        // if the attacker won the fight.
        if (toTerritory.getOwnerID() == fromTerritory.getOwnerID()) {
            MultiplayerModel.INSTANCE.onTerritoryChangedOwner(defenderID, winner[0], toTerritory);
            updateLeaderboard();

            // Remove player from taking turns if they own 0 territories
            if (MultiplayerModel.INSTANCE.getTerritoriesOwnedByPlayer(defenderID).size() == 0) {
                TurnModel.INSTANCE.removePlayer(defenderID);
                // Game over if there is only 1 remaining player
                if (TurnModel.INSTANCE.getNumPlayingPlayers() == 1)
                    GamePresenter.INSTANCE.onGameOver();
            }
        }

        // update the troop observers
        for (TroopObserver observer : troopObservers) {
            observer.onTerritoryChangeNumTroops(fromTerritory);
            observer.onTerritoryChangeNumTroops(toTerritory);
        }
        BoardPresenter.INSTANCE.onTerritoryChangedOwner(toTerritory);
        //Clears the attack HashMap after the attack has gone through.
        AttackModel.INSTANCE.cancelAttack();
        deselectTerritories();
        System.out.println(" - Player" + winner[0] + " won this fight. - ");
    }

    public void exitToMainMenuButtonClicked() {
        // TODO: add message view to ask the player "Are you sure you want to exit?" and "All progress will be lost"
        GamePresenter.INSTANCE.client.setMatchNotActive();
        GamePresenter.INSTANCE.exitToMainMenu();
    }

    /**
     * Updates the leaderboard when a player has conquered another player's territory.
     */
    private void updateLeaderboard() {
        Map<Integer, Set<Territory>> territoriesPerPlayer = MultiplayerModel.INSTANCE.getTerritoriesPerPlayer();

        Map<Integer, Integer> numTerritoriesPerPlayer = new HashMap<>(territoriesPerPlayer.size());
        for (Map.Entry<Integer, Set<Territory>> entry : territoriesPerPlayer.entrySet())
            numTerritoriesPerPlayer.put(entry.getKey(), entry.getValue().size());

        List<Map.Entry<Integer, Integer>> numTerritoriesPerPlayer_sorted = new ArrayList<>(numTerritoriesPerPlayer.entrySet());
        Collections.sort(numTerritoriesPerPlayer_sorted, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                // Sort with decreasing order
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (LeaderboardObserver observer : leaderboardObservers)
            observer.updateLeaderboard(numTerritoriesPerPlayer_sorted);
    }

    public void onTerritoryClicked(Territory territory) {
        //PhaseModel.INSTANCE.getPhase().territoryClicked(territory); //update the model
        // update the view
        final int currentPlayerID = TurnModel.INSTANCE.getCurrentPlayerID();
        final PhaseModel.PhaseState phase = PhaseModel.INSTANCE.getPhase();

        switch (phase.getEnum()) {
            case PLACE:
                int troopsToPlace = AttackModel.INSTANCE.getTroopsToPlace();
                if (troopsToPlace > 0 && territory.getOwnerID() == currentPlayerID) {
                    phase.territoryClicked(territory);
                    AttackModel.INSTANCE.setTroopsToPlace(--troopsToPlace);
                    for (TroopObserver observer : troopObservers)
                        observer.onTerritoryChangeNumTroops(territory);
                    for (PhaseObserver observer : phaseObservers)
                        observer.updateRenderedVariables(phase.getEnum().toString(), troopsToPlace);
                } else {
                    System.out.println("No troops left to place");
                }
                break;

            case ATTACK:
                final Map<String, Integer> attack = AttackModel.INSTANCE.getAttack();
                final Territory fromTerritory = AttackModel.INSTANCE.getFromTerritory();

                if (territory.getOwnerID() == currentPlayerID && territory.getNumTroops() > 1) {
                    if (attack == null || attack.size() == 0) {
                        AttackModel.INSTANCE.setAttackFrom(territory);
                        for (PhaseObserver observer : phaseObservers)
                            observer.addCancelButton();
                    }
                }
                if (attack != null) {
                    if (attack.size() == 1 && territory.getOwnerID() != currentPlayerID) {
                        if (fromTerritory.getNeighbors().contains(territory)) {
                            AttackModel.INSTANCE.setAttackTo(territory);
                            for (PhaseObserver observer : phaseObservers) {
                                observer.onSelectedTerritoriesChange(fromTerritory, territory);
                                observer.addAttackButton();
                            }
                        }
                    } else if (attack.size() == 2 && territory.getOwnerID() != currentPlayerID) {
                        if (fromTerritory.getNeighbors().contains(territory)) {
                            AttackModel.INSTANCE.setAttackTo(territory);
                            for (PhaseObserver observer : phaseObservers)
                                observer.onSelectedTerritoriesChange(fromTerritory, territory);
                        }
                    }
                }
                break;

            case FORTIFY:
                PhaseModel.FortifyPhase fortifyPhase = (PhaseModel.FortifyPhase)phase;
                fortifyPhase.territoryClicked(territory, currentPlayerID);
                //update UI
                if (fortifyPhase.getSelectedFrom() != null && fortifyPhase.getSelectedTo() != null) {
                    for (PhaseObserver observer : phaseObservers) {
                        observer.addFortifyButton();
                        observer.addCancelButton();
                    }
                }
                for (PhaseObserver observer : phaseObservers)
                    observer.onSelectedTerritoriesChange(fortifyPhase.getSelectedFrom(), fortifyPhase.getSelectedTo());
                break;
        }
    }

    public static void reset() {
        INSTANCE._reset();
    }

    private void _reset() {
        AttackModel.reset();
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
