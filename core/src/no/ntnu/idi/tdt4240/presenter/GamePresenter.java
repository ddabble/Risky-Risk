package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.MultiplayerModel;
import no.ntnu.idi.tdt4240.observer.GameObserver;
import no.ntnu.idi.tdt4240.observer.WinObserver;

public class GamePresenter {
    public static final GamePresenter INSTANCE = new GamePresenter();

    private Collection<GameObserver> observers = new ArrayList<>();

    private GamePresenter() {}

    public void init() {
        BoardPresenter.INSTANCE.init();
        PhasePresenter.INSTANCE.init();
    }

    public boolean isGameOver(){
        HashMap<Integer, Integer> leaderboard = MultiplayerModel.INSTANCE.getLeaderboard();
        int countPlayerWithTerritories = 0;
        for (HashMap.Entry entry : leaderboard.entrySet()){
            if ((int)entry.getValue() > 0){
                countPlayerWithTerritories++;
            }
        }
        if (countPlayerWithTerritories <= 1)
            return true;
        return false;
    }
    public void exitToWinScreen(){
        for (GameObserver observer: observers){
            observer.exitToWinScreen();
        }
    }
    public void exitToMainMenu(){
        for (GameObserver observer: observers){
            observer.exitToMainMenu();
        }
    }

    public void reset() {
        BoardPresenter.INSTANCE.reset();
    }

    public static void addObserver(GameObserver observer) {
        INSTANCE.observers.add(observer);
    }
}
