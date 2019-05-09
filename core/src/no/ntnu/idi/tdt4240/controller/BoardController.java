package no.ntnu.idi.tdt4240.controller;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.BoardObserver;
import no.ntnu.idi.tdt4240.observer.TroopObserver;

public class BoardController {
    public static final BoardController INSTANCE = new BoardController();

    private Collection<BoardObserver> boardObservers = new ArrayList<>();
    private Collection<TroopObserver> troopObservers = new ArrayList<>();

    private BoardController() {}

    public static void addObserver(BoardObserver observer) {
        INSTANCE.boardObservers.add(observer);
    }

    public static void addObserver(TroopObserver observer) {
        INSTANCE.troopObservers.add(observer);
    }
}
