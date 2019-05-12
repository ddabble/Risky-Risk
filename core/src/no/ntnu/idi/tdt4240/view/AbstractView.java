package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

public abstract class AbstractView extends ApplicationAdapter {
    private SelectBoxStyle selectStyle;
    private Label.LabelStyle labelStyle;
    private Skin skin;
    private TextButtonStyle textButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;

    private BitmapFont headerFont;
    private BitmapFont slideHeaderFont;
    private BitmapFont slideTextFont;
    private BitmapFont inGameLabelButtonFont;
    private BitmapFont inGameLabelFont;

    protected TextButton createButton(String text) {
        return new TextButton(text, this.textButtonStyle);
    }

    protected TextButton createInGameButton(String text){
        //in game buttons
        TextButton textButton = new TextButton(text, this.textButtonStyle);
        //this.textButtonStyle.fontColor = Color.BLACK;
        Label label = this.createLabel(text);
        Label.LabelStyle inGameButtonLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));;
        inGameButtonLabelStyle.font = inGameLabelButtonFont;
        label.setStyle(inGameButtonLabelStyle);
        label.setAlignment(Align.center);
        textButton.setLabel(label);
        return textButton;
    }
    protected TextButton createTextButton(String text, Label.LabelStyle labelStyle, Color fontColor) {
        TextButton textButton = new TextButton(text, this.textButtonStyle);
        this.textButtonStyle.fontColor = fontColor;
        Label label = this.createLabel(text);
        label.setStyle(labelStyle);
        label.setAlignment(Align.center);
        textButton.setLabel(label);
        return textButton;
    }

    protected TextButton createButton(String text, TextButtonStyle textButtonStyle) {
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


    protected Label createLabel(String text, Label.LabelStyle labelStyle) {
        return new Label(text, labelStyle);
    }


    protected TextField createTextField(String text) {
        return new TextField(text,textFieldStyle);
    }

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("button/uiskin.json"));

        createBitmapFonts();

        textButtonStyle = new TextButtonStyle(skin.get(TextButtonStyle.class));
        selectStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        labelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        textFieldStyle = new TextField.TextFieldStyle(skin.get(TextField.TextFieldStyle.class));

        textButtonStyle.up = skin.getDrawable("default-round");
        textButtonStyle.down = skin.getDrawable("default-round-down");
        textButtonStyle.font = skin.getFont("default-font");

        selectStyle.font = skin.getFont("default-font");
    }



    private void createBitmapFonts(){
        // Creates BitmapFonts for texts
        headerFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf",40);
        slideHeaderFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf",25);
        slideTextFont = createBitmapFont("fonts/open-sans/OpenSans-Regular.ttf",20);
        inGameLabelButtonFont = createBitmapFont("fonts/open-sans/OpenSans-Regular.ttf", Gdx.graphics.getHeight()/25);
        inGameLabelFont = createBitmapFont("fonts/open-sans/OpenSans-Regular.ttf", Gdx.graphics.getHeight()/10);

    }
    private BitmapFont createBitmapFont(String path, int size){
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = size;
        BitmapFont result = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
        return result;
    }





    @Override
    public void dispose() {
        skin.dispose();
    }
}
