package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;

import no.ntnu.idi.tdt4240.util.TutorialSlide;


public class TutorialModel {
    private static TutorialModel INSTANCE;

    public final TutorialSlide TUTORIAL_SLIDES;

    private TutorialModel() {
        TUTORIAL_SLIDES = TutorialSlide.parseJsonTutorialSlides(Gdx.files.internal("tutorial/tutorial.json"));
    }

    public static void init() {
        if (INSTANCE != null)
            return;
        INSTANCE = new TutorialModel();
    }

    public static TutorialModel getInstance() {
        return INSTANCE;
    }
}
