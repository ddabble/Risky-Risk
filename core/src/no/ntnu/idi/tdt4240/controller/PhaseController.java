package no.ntnu.idi.tdt4240.controller;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;

public class PhaseController {
    public static final PhaseController INSTANCE = new PhaseController();

    private Collection<PhaseObserver> observers = new ArrayList<>();

    private PhaseController() {}

    public void init() {
        for (PhaseObserver observer : observers) {
            observer.create();
            updatePhase(observer);
        }
    }

    public void updatePhase(PhaseObserver observer) {
        String currentPhase = PhaseModel.INSTANCE.getPhase().getName();
        String nextPhase = PhaseModel.INSTANCE.getPhase().next().getName();
        observer.updatePhase(currentPhase, nextPhase);
    }

    public void onTerritoryClicked(Territory territory) {
        // Update territory based on the phase we are in
        PhaseModel.INSTANCE.getPhase().territoryClicked(territory);
    }

    public void nextPhaseButtonClicked() {
        PhaseModel.INSTANCE.nextPhase();
        for (PhaseObserver observer : observers)
            updatePhase(observer);
    }

    public static void addObserver(PhaseObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
