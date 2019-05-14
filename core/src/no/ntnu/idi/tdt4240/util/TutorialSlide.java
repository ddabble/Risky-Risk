package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class TutorialSlide {
    // TutorialSlide is a utility class for fetching the tutorial text from a json file

    private ArrayList<Map<String, String>> tutorialSlides;

    public TutorialSlide(ArrayList<Map<String, String>> tutorialSlides) {
        this.tutorialSlides = tutorialSlides;
    }

    public ArrayList<Map<String, String>> getTutorialSlides() {
        return this.tutorialSlides;
    }

    public static TutorialSlide parseJsonTutorialSlides(FileHandle jsonFile) {
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

        for (int i = 0; i < slides.size(); i++) {
            tutorialSlides.add(slides.get(Integer.toString(i + 1)));

            // ' * ' is used in the json file to mark a new line
            tutorialSlides.get(i).put("text", tutorialSlides.get(i).get("text").replace(" * ", "\n"));
        }
        return new TutorialSlide(tutorialSlides);
    }
}
