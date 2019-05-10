package no.ntnu.idi.tdt4240.controller;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.MenuObserver;

public class MenuController {
    public static final MenuController INSTANCE = new MenuController();

    private Collection<MenuObserver> observers = new ArrayList<>();

    private MenuController() {}

    public static void addObserver(MenuObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
