package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GamePresenter {
    public static final GamePresenter INSTANCE = new GamePresenter();

    private Collection<GameObserver> observers = new ArrayList<>();

    private GamePresenter() {}

    public void init() {
        BoardPresenter.INSTANCE.init();
        PhasePresenter.INSTANCE.init();
    }

    public void reset() {
        BoardPresenter.INSTANCE.reset();
    }

    /*
    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        view.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    */

    public static void addObserver(GameObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
