package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TutorialSlide {
    private ArrayList<Map<String, String>> tutorialSlides;

    public TutorialSlide(ArrayList<Map<String, String>> tutorialSlides){
        this.tutorialSlides = tutorialSlides;
    }

    public static TutorialSlide parseJsonTutorialSlides(FileHandle jsonFile){
        ObjectMapper mapper = new ObjectMapper();
        Map readJson;
        try {
            readJson = mapper.readValue(jsonFile.readString(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        @SuppressWarnings("unchecked")
        Map<String, Map<String, String>> slides = readJson;
        ArrayList<Map<String, String>> tutorialSlides = new ArrayList<>();

        for(int i=1; i< slides.size()+1; i++){
            tutorialSlides.add(slides.get(Integer.toString(i)));
            System.out.println(tutorialSlides.get(i));
        }
        TutorialSlide tutorialSlide = new TutorialSlide(tutorialSlides);
        return tutorialSlide;
    }

}
