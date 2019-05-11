package no.ntnu.idi.tdt4240.controller;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.model.TutorialModel;
import no.ntnu.idi.tdt4240.observer.TutorialObserver;

public class TutorialController {
    public static final TutorialController INSTANCE = new TutorialController();

    private Collection<TutorialObserver> tutorialObservers = new ArrayList<>();

    private TutorialController(){}

    public void init() {
        TutorialModel.INSTANCE.init();
        for (TutorialObserver observer : this.tutorialObservers){
            observer.create(TutorialModel.INSTANCE.getTutorialSlides());
        }
    }

    public static void addObserver(TutorialObserver tutorialObserver){
        INSTANCE.tutorialObservers.add(tutorialObserver);
    }
}
