package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.observer.TutorialObserver;
import no.ntnu.idi.tdt4240.presenter.TutorialPresenter;

public class TutorialView extends AbstractView implements TutorialObserver, Screen {
    private static final Color FONT_COLOR = new Color(0x0A0A0AFF);

    private final RiskyRisk game;
    private Stage stage;


    private int stage_width;
    private int stage_height;
    private int currentSlideCounter;

    private BitmapFont headerFont;
    private BitmapFont slideHeaderFont;
    private BitmapFont slideTextFont;

    private Label.LabelStyle headerStyle;
    private Label.LabelStyle slideHeaderStyle;
    private Label.LabelStyle slideTextStyle;

    private Label header;
    private Label slideHeader;
    private Label slideText;

    private TextButton previousButton;
    private TextButton nextButton;
    private TextButton mainMenuButton;

    private Texture slideImage;

    private ArrayList<Map<String, String>> tutorialSlides;


    public TutorialView(RiskyRisk game) {
        this.game = game;
        TutorialPresenter.addObserver(this);
    }


    @Override
    public void create(ArrayList<Map<String, String>> tutorialSlides) {
        this.tutorialSlides = tutorialSlides;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(233 / 255f, 230 / 255f, 185 / 255f, 1);

        // Render the tutorial slide image
        int imageWidth = this.stage_width / 2 - this.stage_width / 20;
        int imageHeight = 2 * imageWidth / 3;
        stage.getBatch().begin();
        stage.getBatch().draw(this.slideImage,
                              this.stage_width - (imageWidth + this.stage_width / 40f),
                              this.stage_height - (imageHeight + 150),
                              imageWidth, imageHeight);
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();


    }

    @Override
    public void show() {
        super.create();
        TutorialPresenter.INSTANCE.init();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.slideImage = new Texture(this.tutorialSlides.get(0).get("image"));

        this.stage_width = Gdx.graphics.getWidth();
        this.stage_height = Gdx.graphics.getHeight();

        this.createBitmapFonts();
        this.createTextStyles();
        this.createButtons(stage);
        this.createTextFields(stage);
    }

    private void updateCurrentSlide(boolean increment) {
        // Updates and controls the value of updateCurrentSlide

        if (increment && this.currentSlideCounter < this.tutorialSlides.size() - 1) {
            this.currentSlideCounter += 1;
        } else if (!increment && this.currentSlideCounter > 0) {
            this.currentSlideCounter -= 1;
        }
        this.updateButtonState();
    }

    private void createButtons(Stage stage) {

        // Button sizes
        int btnHeight = 100;
        int btnWidth = 350;

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = this.slideTextFont;
        Color fontColor = new Color(Color.BLACK);


        // Main menu button
        this.mainMenuButton = this.createTextButton("Back to main", labelStyle, fontColor);
        this.mainMenuButton.setPosition(this.stage_width / 100f,
                                        this.stage_height / 50f);
        this.mainMenuButton.setSize(btnWidth, btnHeight);

        // Previous button
        this.previousButton = this.createTextButton("Previous", labelStyle, fontColor);
        this.previousButton.setPosition(this.stage_width - 2 * (btnWidth + this.stage_width / 100f),
                                        this.stage_height / 50f);
        this.previousButton.setSize(btnWidth, btnHeight);
        this.previousButton.setTouchable(Touchable.disabled);

        // Next button
        this.nextButton = this.createTextButton("Next", labelStyle, fontColor);
        this.nextButton.setPosition(this.stage_width - (btnWidth + this.stage_width / 100f),
                                    this.stage_height / 50f);
        this.nextButton.setSize(btnWidth, btnHeight);


        // Add event listeners to buttons
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(RiskyRisk.ScreenEnum.MAIN_MENU);
            }
        });

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateCurrentSlide(false);
                slideHeader.setText(tutorialSlides.get(currentSlideCounter).get("title"));
                slideText.setText(tutorialSlides.get(currentSlideCounter).get("text"));
                slideImage = new Texture(tutorialSlides.get(currentSlideCounter).get("image"));
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateCurrentSlide(true);
                slideHeader.setText(tutorialSlides.get(currentSlideCounter).get("title"));
                slideText.setText(tutorialSlides.get(currentSlideCounter).get("text"));
                slideImage = new Texture(tutorialSlides.get(currentSlideCounter).get("image"));
            }
        });

        // Add buttons to stage
        stage.addActor(mainMenuButton);
        stage.addActor(previousButton);
        stage.addActor(nextButton);

    } // End createButtons()

    private void updateButtonState() {
        // Disables the buttons when at the beginning/end of the slides

        if (this.currentSlideCounter == this.tutorialSlides.size() - 1) {
            this.nextButton.setTouchable(Touchable.disabled);
        } else if (this.currentSlideCounter == 0) {
            this.previousButton.setTouchable(Touchable.disabled);
        } else {
            this.nextButton.setTouchable(Touchable.enabled);
            this.previousButton.setTouchable(Touchable.enabled);
        }
    }


    private void createTextFields(Stage stage) {


        // Text field dimensions
        int headerWidth = 350;
        int headerHeight = 50;

        int slideHeaderWidth = this.stage_width / 2;
        int slideHeaderHeight = 50;

        int tutSlideWidth = this.stage_width / 2 - this.stage_width / 20;
        int tutSlideHeight = 2 * this.stage_height / 3;

        this.currentSlideCounter = 0;

        // Header text
        this.header = this.createLabel("Tutorial", this.headerStyle);
        this.header.setPosition(this.stage_width / 2f - headerWidth / 2f,
                                this.stage_height - (headerHeight + this.stage_height / 30f));
        this.header.setSize(headerWidth, headerHeight);

        // Slide header text
        this.slideHeader = this.createLabel(this.tutorialSlides.get(this.currentSlideCounter).get("title"), this.slideHeaderStyle);
        this.slideHeader.setPosition(this.stage_width / 40f,
                                     this.stage_height - (slideHeaderHeight + 150));
        this.slideHeader.setSize(slideHeaderWidth, slideHeaderHeight);

        // Tutorial slide text
        this.slideText = this.createLabel(this.tutorialSlides.get(this.currentSlideCounter).get("text"), this.slideTextStyle);
        this.slideText.setWrap(true);
        this.slideText.setAlignment(Align.topLeft);
        this.slideText.setPosition(this.stage_width / 40f,
                                   this.stage_height - (tutSlideHeight + slideHeaderHeight + 170));
        slideText.setSize(tutSlideWidth, tutSlideHeight);

        // Add actors
        stage.addActor(header);
        stage.addActor(slideHeader);
        stage.addActor(slideText);
    }

    private void createBitmapFonts() {
        // Creates BitmapFonts for the text fields

        // Header font
        FreeTypeFontGenerator headerGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headerParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParameter.size = 80;
        this.headerFont = headerGenerator.generateFont(headerParameter);
        headerGenerator.dispose();

        // Slide header font
        FreeTypeFontGenerator slideHeaderGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter slideHeaderParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        slideHeaderParameter.size = 60;
        this.slideHeaderFont = slideHeaderGenerator.generateFont(slideHeaderParameter);
        slideHeaderGenerator.dispose();

        // Slide text font
        FreeTypeFontGenerator slideTextGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter slideTextParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        slideTextParameter.size = 45;
        this.slideTextFont = slideTextGenerator.generateFont(slideTextParameter);
        slideTextGenerator.dispose();
    }

    private void createTextStyles() {
        // Creates text styles for the text fields

        // Header style
        this.headerStyle = new Label.LabelStyle();
        this.headerStyle.font = this.headerFont;
        this.headerStyle.fontColor = FONT_COLOR;

        // Slide header style
        this.slideHeaderStyle = new Label.LabelStyle();
        this.slideHeaderStyle.font = this.slideHeaderFont;
        this.slideHeaderStyle.fontColor = FONT_COLOR;

        // Slide text style
        this.slideTextStyle = new Label.LabelStyle();
        this.slideTextStyle.font = this.slideTextFont;
        this.slideTextStyle.fontColor = FONT_COLOR;
    }

    @Override
    public void hide() {
        slideImage.dispose();
        stage.dispose();

        slideTextFont.dispose();
        slideHeaderFont.dispose();
        headerFont.dispose();

        super.dispose();
    }
} // End class
