package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.WinObserver;

public class WinPresenter {
    public static final WinPresenter INSTANCE = new WinPresenter() {};

    private Collection<WinObserver> observers = new ArrayList<>();

    private WinPresenter() {}

    public static void addObserver(WinObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
