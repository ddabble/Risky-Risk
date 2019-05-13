package no.ntnu.idi.tdt4240.model;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.PhaseEnum;

public class PhaseModel {
    public static final PhaseModel INSTANCE = new PhaseModel();

    private PhaseState phase;

    private PhaseModel() {
        // Initial phase state
        phase = new PlacePhase();
    }

    public PhaseState getPhase() {
        return phase;
    }

    public void nextPhase() {
        phase = phase.next();
        // TODO: phase.create(),  to phase-specific UI creation.
    }

    // Phases
    public interface PhaseState {
        PhaseEnum getEnum();

        PhaseState next();

        void territoryClicked(Territory territory);
    }

    private class PlacePhase implements PhaseState {
        @Override
        public PhaseEnum getEnum() {
            return PhaseEnum.PLACE;
        }

        @Override
        public PhaseState next() {
            return new AttackPhase();
        }

        @Override
        public void territoryClicked(Territory territory) {
            territory.setNumTroops(territory.getNumTroops() + 1);
        }
    }

    private class AttackPhase implements PhaseState {
        @Override
        public PhaseEnum getEnum() {
            return PhaseEnum.ATTACK;
        }

        @Override
        public PhaseState next() {
            return new FortifyPhase();
        }

        @Override
        public void territoryClicked(Territory territory) {
            return;
        }
    }

    public class FortifyPhase implements PhaseState {

        private Territory selectedFrom;
        private Territory selectedTo;
        private int nextCount = 0;

        private void start() { //called when the phase is started

            System.out.println("Started fortify phase");
        }

        @Override
        public PhaseEnum getEnum() {
            return PhaseEnum.FORTIFY;
        }

        @Override
        public PhaseState next() { //called in the beginning.
            if (nextCount == 0) {
                nextCount++;
                start();
            }
            return new PlacePhase();
        }

        public Territory getSelectedFrom() {
            return selectedFrom;
        }

        public Territory getSelectedTo() {
            return selectedTo;
        }

        public void clearTerritorySelection() {
            selectedFrom = null;
            selectedTo = null;
        }

        @Override
        public void territoryClicked(Territory territory) {
            return;
        }

        public void territoryClicked(Territory territory, int currentPlayerID) {
            //territory.setNumTroops(territory.getNumTroops() + 1);
            if (territory.getOwnerID() == currentPlayerID) {
                if (selectedFrom == null && territory.getNumTroops() > 1) {
                    selectedFrom = territory;
                } else if (selectedTo == null && territory != null && selectedFrom != null) { //at this point the from is selected
                    if (isConnected(selectedFrom, territory))
                        selectedTo = territory;
                } else if (selectedTo != null && selectedTo != selectedFrom && selectedFrom != territory &&
                           selectedFrom != null && territory != null) {
                    if (isConnected(selectedFrom, territory))
                        selectedTo = territory;
                }
            }

            //update the ui, show what is being selected.
        }

        /**
         * DFS to find if the territories are connected
         *
         * @param t1 Territory
         * @param t2 Territory
         *
         * @return List of visited territories.
         */
        private boolean isConnected(Territory t1, Territory t2) {
            return dfs(t1, t2, new ArrayList<>());
        }

        private boolean dfs(Territory t1, Territory t2, List<Territory> visited) { //boolean indicate found or not
            if (!visited.contains(t1)) {
                visited.add(t1);
                if (t1 == t2) // if the current visiting territory is the goal
                    return true;
                for (Territory t : t1.getNeighbors()) {
                    if (t.getOwnerID() == t2.getOwnerID() && dfs(t, t2, visited)) {
                        return true;
                    }
                }
                return false;
            } else {
                return false;
            }
        }
    }
}
