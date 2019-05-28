package no.ntnu.idi.tdt4240.view.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;

public class UIStyle {
    public static final UIStyle INSTANCE = new UIStyle();

    private static final String REGULAR_FONT_PATH = "fonts/open-sans/OpenSans-Regular.ttf";
    private static final String BOLD_FONT_PATH = "fonts/open-sans/OpenSans-Bold.ttf";

    private Skin skin;

    private TextButton.TextButtonStyle textButtonStyle;
    private SelectBox.SelectBoxStyle selectStyle;
    private Label.LabelStyle labelStyle;
    private TextField.TextFieldStyle textFieldStyle;

    private BitmapFont standardButtonFont;
    private BitmapFont tutorialHeaderFont;
    private BitmapFont tutorialSlideHeaderFont;
    private BitmapFont tutorialSlideTextFont;
    private BitmapFont inGameLabelFont;
    private BitmapFont inGamePlayerColorableFont;
    private BitmapFont leaderboardFont;
    private BitmapFont troopNumFont;

    private UIStyle() {}

    public static int getStandardButtonFontSize() {
        return Gdx.graphics.getHeight() / 24;
    }

    public static void init() {
        INSTANCE._init();
    }

    private void _init() {
        skin = new Skin(Gdx.files.internal("button/uiskin.json"));

        createBitmapFonts();

        textButtonStyle = new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
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
        standardButtonFont = createStandardButtonFont();
        tutorialHeaderFont = createBoldFont((int)(Gdx.graphics.getHeight() / 13.5f));
        tutorialSlideHeaderFont = createBoldFont(Gdx.graphics.getHeight() / 18);
        tutorialSlideTextFont = createRegularFont(Gdx.graphics.getHeight() / 24);
        inGameLabelFont = createBoldFont(Gdx.graphics.getHeight() / 22);
        leaderboardFont = createBoldFont(Gdx.graphics.getHeight() / 30);
        troopNumFont = createBoldFont(Gdx.graphics.getHeight() / 32);

        // inGame label font
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.borderColor = new Color(0.3f, 0.3f, 0.3f, 0.8f);
        fontParameter.borderWidth = 3f;
        inGamePlayerColorableFont = createFont(BOLD_FONT_PATH, fontParameter,
                                               Gdx.graphics.getHeight() / 20);
    }

    public BitmapFont createStandardButtonFont() {
        return createRegularFont(getStandardButtonFontSize());
    }

    public BitmapFont createRegularFont(int size) {
        return createFont(REGULAR_FONT_PATH, size);
    }

    public BitmapFont createBoldFont(int size) {
        return createFont(BOLD_FONT_PATH, size);
    }

    private BitmapFont createFont(String path, int size) {
        return createFont(path, new FreeTypeFontGenerator.FreeTypeFontParameter(), size);
    }

    private BitmapFont createFont(String path, FreeTypeFontGenerator.FreeTypeFontParameter fontParameter, int size) {
        fontParameter.size = size;
        fontParameter.color = new Color(Color.WHITE);
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(path));
        BitmapFont result = fontGenerator.generateFont(fontParameter);
        fontGenerator.dispose();
        return result;
    }

    public TextField.TextFieldStyle createTroopTextStyle(Color fontColor) {
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = troopNumFont;
        textStyle.fontColor = fontColor;
        return textStyle;
    }

    public Label createTutorialHeaderLabel(String text, Color fontColor) {
        return createLabel(text, tutorialHeaderFont, fontColor);
    }

    public Label createTutorialSlideHeaderLabel(String text, Color fontColor) {
        return createLabel(text, tutorialSlideHeaderFont, fontColor);
    }

    public Label createTutorialSlideTextLabel(String text, Color fontColor) {
        return createLabel(text, tutorialSlideTextFont, fontColor);
    }

    public Label createLeaderboardLabel(String text) {
        return createLabel(text, leaderboardFont, null);
    }

    public Label createInGameLabel(String text) {
        return createLabel(text, inGameLabelFont, null);
    }

    public Label createPlayerColorableLabel(String text) {
        return createLabel(text, inGamePlayerColorableFont, null);
    }

    public Label createLabel(String text) {
        return createLabel(text, null, null);
    }

    private Label createLabel(String text, BitmapFont font, Color fontColor) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(this.labelStyle);
        if (font != null)
            labelStyle.font = font;
        if (fontColor != null)
            labelStyle.fontColor = fontColor;

        return createLabel(text, labelStyle);
    }

    public Label createLabel(String text, Label.LabelStyle style) {
        Label label = new Label(text, style);
        label.setAlignment(Align.center);
        return label;
    }

    public TextButton createTutorialButton(String text, Color fontColor) {
        return createTextButton(text, tutorialSlideTextFont, fontColor);
    }

    public TextButton createTextButton(String text) {
        return createTextButton(text, standardButtonFont);
    }

    public TextButton createTextButton(String text, BitmapFont font) {
        return createTextButton(text, font, null);
    }

    public TextButton createTextButton(String text, BitmapFont font, Color fontColor) {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(this.textButtonStyle);
        if (fontColor != null)
            textButtonStyle.fontColor = fontColor;

        TextButton textButton = new TextButton(text, textButtonStyle);
        textButton.setLabel(createLabel(text, font, null));
        return textButton;
    }

    public static void dispose() {
        INSTANCE._dispose();
    }

    private void _dispose() {
        troopNumFont.dispose();
        leaderboardFont.dispose();
        inGamePlayerColorableFont.dispose();
        inGameLabelFont.dispose();
        tutorialSlideTextFont.dispose();
        tutorialSlideHeaderFont.dispose();
        tutorialHeaderFont.dispose();
        standardButtonFont.dispose();

        skin.dispose();
    }
}
