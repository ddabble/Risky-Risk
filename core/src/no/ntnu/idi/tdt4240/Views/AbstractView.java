package no.ntnu.idi.tdt4240.Views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;


import no.ntnu.idi.tdt4240.RiskyRisk;

public abstract class AbstractView implements Screen  {
    private final RiskyRisk riskyRisk;
    private final SelectBoxStyle selectStyle;
    private final Label.LabelStyle labelStyle;
    Skin skin;
    TextButtonStyle textButtonStyle;

    public AbstractView(RiskyRisk controller) {
        this.riskyRisk = controller;
        skin = new Skin(Gdx.files.internal("button/uiskin.json"));
        textButtonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        selectStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        labelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));

        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");
        textButtonStyle.font = skin.getFont("default-font");

        selectStyle.font = skin.getFont("default-font");
    }

    protected Button createButton(String text) {
        Button button = new TextButton(text, this.textButtonStyle);
        return button;
    }

    protected SelectBox<String> createSelectBox(String[] options) {
        SelectBox<String> selectBox = new SelectBox<String>(selectStyle);
        selectBox.setItems(options);
        return selectBox;
    }

    protected Label createLabel(String text) {
        return new Label(text, this.labelStyle);
    }

}
