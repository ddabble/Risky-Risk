package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;



import no.ntnu.idi.tdt4240.RiskyRisk;

public abstract class AbstractView implements Screen  {
    private final RiskyRisk game;
    private final SelectBoxStyle selectStyle;
    Skin skin;
    TextButtonStyle textButtonStyle;

    public AbstractView(RiskyRisk game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal("button/uiskin.json"));
        textButtonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        selectStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));

        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");
        textButtonStyle.font = game.font;

        selectStyle.font = game.font;
    }

    public Button createButton(String text) {
        Button button = new TextButton(text, this.textButtonStyle);
        return button;
    }

    public SelectBox<String> createSelectBox(String[] options) {
        SelectBox<String> selectBox = new SelectBox<String>(selectStyle);
        selectBox.setItems(options);
        return selectBox;
    }

}
