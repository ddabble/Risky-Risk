package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;

public class MenuStateComponent implements Component {
    public enum State {
        MainMenu, SettingsMenu, MultiplayerMenu, GameMenu, TutorialMenu
    };


    private State currentState;

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State state){
        this.currentState = state;
    }
}
