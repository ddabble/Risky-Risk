package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.Map;

import no.ntnu.idi.tdt4240.util.TutorialSlide;


public class TutorialModel {
    // TutorialModel uses the utility class TutorialSlide to get the tutorial text from a json file
    //  and makes it available from the getTutorialSlides() method

    public static TutorialModel INSTANCE;

    private final TutorialSlide TUTORIAL_SLIDES;

    private TutorialModel() {
        TUTORIAL_SLIDES = TutorialSlide.parseJsonTutorialSlides(Gdx.files.internal("tutorial/tutorial.json"));
    }

    public static TutorialModel getInstance() {
        return INSTANCE;
    }

    public static ArrayList<Map<String, String>> getTutorialSlides() {
        return INSTANCE.TUTORIAL_SLIDES.getTutorialSlides();
    }

    public static void init() {
        if (INSTANCE != null)
            return;
        INSTANCE = new TutorialModel();
    }
}
