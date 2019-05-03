package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import no.ntnu.idi.tdt4240.RiskyRisk;

public abstract class AbstractView implements Screen  {
    private final TextField.TextFieldStyle textFieldStyle;
    protected RiskyRisk game;
    private final SelectBoxStyle selectStyle;
    private final Label.LabelStyle labelStyle;
    private Skin skin;
    private TextButtonStyle textButtonStyle;

    public AbstractView(RiskyRisk game) {
        this.game = game;

        // Skins
        skin = new Skin(Gdx.files.internal("button/uiskin.json"));
        textButtonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        selectStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        labelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        textFieldStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));

        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");
        textButtonStyle.font = skin.getFont("default-font");

        selectStyle.font = skin.getFont("default-font");
    }

    protected Button createButton(String text) {
        Button button = new TextButton(text, this.textButtonStyle);
        return button;
    }

    protected <T> SelectBox<T> createSelectBox(T[] options) {
        SelectBox<T> selectBox = new SelectBox<T>(selectStyle);
        selectBox.setItems(options);
        return selectBox;
    }

    protected TextField createTextField(String text) {
        return new TextField(text,textFieldStyle);
    }

    protected Label createLabel(String text) {
        return new Label(text, this.labelStyle);
    }
}
