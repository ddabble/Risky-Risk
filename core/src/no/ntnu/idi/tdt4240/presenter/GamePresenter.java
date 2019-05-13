package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GamePresenter {
    public static final GamePresenter INSTANCE = new GamePresenter();

    private Collection<GameObserver> observers = new ArrayList<>();

    private GamePresenter() {}

    public void init() {
        BoardPresenter.INSTANCE.init();
        PhasePresenter.INSTANCE.init();
    }

    public boolean isGameOver() {
        Map<Integer, Integer> leaderboard = MultiplayerModel.INSTANCE.getLeaderboard();
        int countPlayerWithTerritories = 0;
        for (Map.Entry entry : leaderboard.entrySet()) {
            if ((int)entry.getValue() > 0) {
                countPlayerWithTerritories++;
            }
        }
        return countPlayerWithTerritories <= 1;
    }

    public void exitToWinScreen() {
        for (GameObserver observer : observers) {
            observer.exitToWinScreen();
        }
    }

    public void exitToMainMenu() {
        for (GameObserver observer : observers) {
            observer.exitToMainMenu();
        }
    }

    public void reset() {
        PhasePresenter.INSTANCE.reset();
        BoardPresenter.INSTANCE.reset();
    }

    public static void addObserver(GameObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
