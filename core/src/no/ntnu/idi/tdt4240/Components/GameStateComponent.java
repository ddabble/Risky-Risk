package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;

public class GameStateComponent implements Component {

    public enum gameState {
        Playing, Waiting
    }

    private gameState currentGameState;

    public gameState getCurrentGameState() {
        return currentGameState;
    }
    public void setCurrentGameState(gameState currentGameState){
        this.currentGameState = currentGameState;
    }
}
