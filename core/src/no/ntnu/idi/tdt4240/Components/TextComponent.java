package no.ntnu.idi.tdt4240.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class TextComponent implements Component {
    private BitmapFont font = new BitmapFont();
    private String text;

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public BitmapFont getFont() {
        return font;
    }
}
