package no.ntnu.idi.tdt4240.presenter;

import java.util.ArrayList;
import java.util.Collection;

import no.ntnu.idi.tdt4240.model.TutorialModel;
import no.ntnu.idi.tdt4240.observer.TutorialObserver;

public class TutorialPresenter {
    public static final TutorialPresenter INSTANCE = new TutorialPresenter();

    private Collection<TutorialObserver> tutorialObservers = new ArrayList<>();

    private TutorialPresenter() {}

    public static void init() {
        INSTANCE._init();
    }

    private void _init() {
        TutorialModel.init();
        for (TutorialObserver observer : this.tutorialObservers) {
            observer.create(TutorialModel.getTutorialSlides());
        }
    }

    public static void addObserver(TutorialObserver tutorialObserver) {
        INSTANCE.tutorialObservers.add(tutorialObserver);
    }
}
