package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.observer.GameObserver;

public class GamePresenter {
    public static final GamePresenter INSTANCE = new GamePresenter();

    private Collection<GameObserver> observers = new ArrayList<>();

    private IGPGSClient client;

    private GamePresenter() {}

    public static void init(OrthographicCamera camera, IGPGSClient client) {
        INSTANCE._init(camera, client);
    }

    private void _init(OrthographicCamera camera, IGPGSClient client) {
        BoardPresenter.init(camera, client);
        this.client = client;

        PhasePresenter.init(camera);
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

    public static void reset() {
        INSTANCE._reset();
    }

    private void _reset() {
        PhasePresenter.reset();
        BoardPresenter.reset();
    }

    public static void addObserver(GameObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
