package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.MenuObserver;

public class MenuPresenter {
    public static final MenuPresenter INSTANCE = new MenuPresenter();

    private Collection<MenuObserver> observers = new ArrayList<>();

    private MenuPresenter() {}

    public static void addObserver(MenuObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
