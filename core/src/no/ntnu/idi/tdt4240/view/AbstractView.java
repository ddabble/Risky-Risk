package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public abstract class AbstractView extends ApplicationAdapter {
    private SelectBoxStyle selectStyle;
    private Label.LabelStyle labelStyle;
    private Skin skin;
    private TextButtonStyle textButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;

    protected TextButton createButton(String text) {
        return new TextButton(text, textButtonStyle);
    }

    protected <T> SelectBox<T> createSelectBox(T[] options) {
        SelectBox<T> selectBox = new SelectBox<>(selectStyle);
        selectBox.setItems(options);
        return selectBox;
    }

    protected Label createLabel(String text) {
        return new Label(text, labelStyle);
    }


    protected TextField createTextField(String text) {
        return new TextField(text,textFieldStyle);
    }

    @Override
    public void create() {
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

    @Override
    public void dispose() {
        skin.dispose();
    }
}
