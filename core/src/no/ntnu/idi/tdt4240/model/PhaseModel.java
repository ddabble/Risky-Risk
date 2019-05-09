package no.ntnu.idi.tdt4240.model;

import no.ntnu.idi.tdt4240.data.Territory;

public class PhaseModel {
    public static final PhaseModel INSTANCE = new PhaseModel();

    private PhaseState phase;

    private PhaseModel() {
        // Initial phase state
        phase = new SetupPhase();
    }

    public PhaseState getPhase() {
        return phase;
    }

    public void nextPhase() {
        phase = phase.next();
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

    private class FortifyPhase implements PhaseState {
        @Override
        public String getName() {
            return "Fortify";
        }

        @Override
        public PhaseState next() {
            return new PlacePhase();
        }

        @Override
        public void territoryClicked(Territory territory) {
            territory.setNumTroops(territory.getNumTroops() + 1);
        }
    }
}
