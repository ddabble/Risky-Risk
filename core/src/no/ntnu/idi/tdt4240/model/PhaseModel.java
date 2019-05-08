package no.ntnu.idi.tdt4240.model;


import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.ntnu.idi.tdt4240.data.Territory;

public class PhaseModel {
    private PhaseState phase;
    // TODO: enums for phases instead of string names

    public PhaseModel() {
        // Initial phase state
        phase = new SetupPhase();
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
        String getName();

        PhaseState next();

        // TODO: write actual implementation
        void territoryClicked(Territory territory);
    }

    private class SetupPhase implements PhaseState {
        @Override
        public String getName() {
            return "Set-up";
        }

        @Override
        public PhaseState next() {
            return new PlacePhase();
        }

        @Override
        public void territoryClicked(Territory territory) {
            // TODO: debugging code:
            territory.setNumTroops(territory.getNumTroops() + 1);
        }
    }

    private class PlacePhase implements PhaseState {
        @Override
        public String getName() {
            return "Place";
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
        public String getName() {
            return "Attack";
        }

        @Override
        public PhaseState next() {
            return new FortifyPhase();
        }

        @Override
        public void territoryClicked(Territory territory) {
            territory.setNumTroops(territory.getNumTroops() + 1);
        }
    }

    public class FortifyPhase implements PhaseState {

        private int myID = 1; //  MOCK the playerID of the user. Different users have different ids
        private Territory selectedFrom;
        private Territory selectedTo;
        private int nextCount = 0;

        @Override
        public String getName() {
            return "Fortify";
        }

        private void start() { //called when the phase is started

            System.out.println("Started fortify phase");
        }

        private void end() { //called when the phase is ending
            // TODO: warning when going next without selecting anything
            //do fortification
            if(selectedFrom == null) //do nothing when nothing selected
                return;
            FortifyModel.move(myID, selectedFrom, selectedTo, 3);
            // TODO: UI to change num of troops to fortify
            System.out.println("Ended fortify phase");

        }

        @Override
        public PhaseState next() { //called in the beginning.
            if (nextCount == 0) {
                nextCount++;
                start();
            } else {
                end();
            }
            return new PlacePhase();
        }

        public Territory getSelectedFrom() {
            return selectedFrom;
        }

        public Territory getSelectedTo() {
            return selectedTo;
        }

        @Override
        public void territoryClicked(Territory territory) {
            territory.setNumTroops(territory.getNumTroops() + 1);
            if (territory.getOwnerID() != myID) {
                System.out.println("You have selected a territory you don't own.");
            }
            if (selectedFrom == null) {
                selectedFrom = territory;
            } else if (selectedFrom == territory) { //selected the same territory twice.
                selectedFrom = null;
            } else if (selectedTo == null) { //at this point the from is selected
                selectedTo = territory;
            } else {  //after both are set, to reset the selected territories, just pick another territory
                selectedFrom = territory;
                selectedTo = null;
            }
            //update the ui, show what is being selected.


        }

        /**
         * DFS to find if the territories are connected
         *
         * @param t1 Territory
         * @param t2 Territory
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
