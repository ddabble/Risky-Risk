package no.ntnu.idi.tdt4240.controller;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.PhaseObserver;

public class PhaseController {
    public static final PhaseController INSTANCE = new PhaseController();

    private Collection<PhaseObserver> observers = new ArrayList<>();

    private PhaseController() {}

    public static void addObserver(PhaseObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
