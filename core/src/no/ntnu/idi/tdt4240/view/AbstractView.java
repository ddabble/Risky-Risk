package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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
    protected BitmapFont slideTextFont;
    private BitmapFont inGameLabelButtonFont;
    private BitmapFont inGameLabelFont;
    BitmapFont inGamePlayerColorableFont;
    private BitmapFont leaderboardFont;
    BitmapFont troopNumFont;

    protected TextButton createButton(String text) {
        return new TextButton(text, this.textButtonStyle);
    }

    protected TextButton createInGameButton(String text) {
        //in game buttons
        TextButton textButton = new TextButton(text, this.textButtonStyle);
        //this.textButtonStyle.fontColor = Color.BLACK;
        Label label = this.createLabel(text);
        Label.LabelStyle inGameButtonLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
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

    protected Label createLeaderboardLabel(String text) {
        Label label = this.createLabel(text);
        Label.LabelStyle leaderboardLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        leaderboardLabelStyle.font = leaderboardFont;
        label.setStyle(leaderboardLabelStyle);

        return new Label(text, leaderboardLabelStyle);
    }

    protected Label createInGameLabel(String text) {
        Label label = this.createLabel(text);
        Label.LabelStyle inGameLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        inGameLabelStyle.font = inGameLabelFont;
        label.setStyle(inGameLabelStyle);

        return new Label(text, inGameLabelStyle);
    }

    protected Label createPlayerColorableLabel(String text) {
        Label label = this.createLabel(text);
        Label.LabelStyle inGameLabelStyle = new Label.LabelStyle(skin.get(Label.LabelStyle.class));
        inGameLabelStyle.font = inGamePlayerColorableFont;
        label.setStyle(inGameLabelStyle);

        return new Label(text, inGameLabelStyle);
    }


    protected Label createLabel(String text, Label.LabelStyle labelStyle) {
        return new Label(text, labelStyle);
    }

    protected TextField createTextField(String text) {
        return new TextField(text, textFieldStyle);
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


    private void createBitmapFonts() {
        // Creates BitmapFonts for texts
        headerFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf", 40);
        slideHeaderFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf", 25);
        slideTextFont = createBitmapFont("fonts/open-sans/OpenSans-Regular.ttf", 20);
        inGameLabelButtonFont = createBitmapFont("fonts/open-sans/OpenSans-Regular.ttf", Gdx.graphics.getHeight() / 25);
        inGameLabelFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf", Gdx.graphics.getHeight() / 22);
        leaderboardFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf", Gdx.graphics.getHeight() / 30);
        troopNumFont = createBitmapFont("fonts/open-sans/OpenSans-Bold.ttf", Gdx.graphics.getHeight() / 32);

        // inGame label font
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = Gdx.graphics.getHeight() / 20;
        fontParameter.borderColor = new Color(0.3f, 0.3f, 0.3f, 0.8f);
        fontParameter.borderWidth = 3f;
        fontParameter.color = Color.WHITE;
        inGamePlayerColorableFont = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
    }

    private BitmapFont createBitmapFont(String path, int size) {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = size;
        fontParameter.color = Color.WHITE;
        BitmapFont result = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
        return result;
    }


    @Override
    public void dispose() {
        troopNumFont.dispose();
        leaderboardFont.dispose();
        inGamePlayerColorableFont.dispose();
        inGameLabelFont.dispose();
        inGameLabelButtonFont.dispose();
        slideTextFont.dispose();
        slideHeaderFont.dispose();
        headerFont.dispose();

        skin.dispose();
    }
}
