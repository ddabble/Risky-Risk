package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.scenes.scene2d.Stage;

public interface PhaseObserver {
    Stage getStage();

    void create();

    void updatePhase(String currentPhase, String nextPhase);
}
