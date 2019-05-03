package no.ntnu.idi.tdt4240.model;

public class PhaseModel {

    private PhaseState phase;

    public PhaseModel() {
        //Initial phase state
        this.phase = new SetupPhase();
    }

    public PhaseState getPhase() {
        return this.phase;
    }

    public void nextPhase() {
        this.phase = this.phase.next();
    }

    // Temporary method
    public void territoryClicked(int id){
        return;
    }

    // Phases
    public interface PhaseState {
        String getName();
        PhaseState next();
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
    }
}
