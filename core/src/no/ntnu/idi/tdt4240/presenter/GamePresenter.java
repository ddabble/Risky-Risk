package no.ntnu.idi.tdt4240.presenter;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.model.SettingsModel;
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
        this.client = client;

        SettingsModel.setNumPlayers(client.getmRiskyTurn().getNumPlayers());
        MultiplayerModel.init(SettingsModel.INSTANCE.getNumPlayers());

        BoardPresenter.init(camera, client);
        PhasePresenter.init(camera);
    }

    public void onGameOver() {
        for (GameObserver observer : observers)
            observer.exitToWinScreen();
    }

    public void exitToMainMenuButtonClicked() {
        // TODO: add message view to ask the player "Are you sure you want to exit?" and "All progress will be lost"
        client.setMatchNotActive();
        for (GameObserver observer : observers)
            observer.exitToMainMenu();
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
