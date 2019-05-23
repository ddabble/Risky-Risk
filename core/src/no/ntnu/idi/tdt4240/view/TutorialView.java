package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class TutorialView extends ApplicationAdapter implements TutorialObserver, Screen {
    private static final Color FONT_COLOR = new Color(0x0A0A0AFF);

    private final RiskyRisk game;

    private Stage stage;

    private int stage_width;
    private int stage_height;
    private int currentSlideCounter;

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
                              this.slideHeader.getTop() - imageHeight,
                              imageWidth, imageHeight);
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        TutorialPresenter.INSTANCE.init();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.slideImage = new Texture(this.tutorialSlides.get(0).get("image"));

        this.stage_width = Gdx.graphics.getWidth();
        this.stage_height = Gdx.graphics.getHeight();

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
        final float btnHeight = stage_height / 11f;
        final float btnWidth = stage_width / 5.5f;

        Color fontColor = new Color(Color.BLACK);

        // Main menu button
        this.mainMenuButton = UIStyle.INSTANCE.createTutorialButton("Back to Main Menu", fontColor);
        this.mainMenuButton.setPosition(this.stage_width / 100f,
                                        this.stage_height / 50f);
        this.mainMenuButton.setSize(btnWidth * 1.4f, btnHeight);

        // Previous button
        this.previousButton = UIStyle.INSTANCE.createTutorialButton("Previous", fontColor);
        this.previousButton.setPosition(this.stage_width - 2 * (btnWidth + this.stage_width / 100f),
                                        this.stage_height / 50f);
        this.previousButton.setSize(btnWidth, btnHeight);
        this.previousButton.setTouchable(Touchable.disabled);

        // Next button
        this.nextButton = UIStyle.INSTANCE.createTutorialButton("Next", fontColor);
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
        this.currentSlideCounter = 0;

        // Header text
        this.header = UIStyle.INSTANCE.createTutorialHeaderLabel("Tutorial", FONT_COLOR);
        this.header.setPosition(this.stage_width / 2f,
                                this.stage_height * 29f / 30f,
                                Align.top);

        // Slide header text
        String headerText = this.tutorialSlides.get(this.currentSlideCounter).get("title");
        this.slideHeader = UIStyle.INSTANCE.createTutorialSlideHeaderLabel(headerText, FONT_COLOR);
        this.slideHeader.setAlignment(Align.left);
        this.slideHeader.setWidth(this.stage_width / 2f);
        this.slideHeader.setPosition(this.stage_width / 40f,
                                     this.header.getY() - this.slideHeader.getHeight() / 2f,
                                     Align.topLeft);

        // Tutorial slide text
        String bodyText = this.tutorialSlides.get(this.currentSlideCounter).get("text");
        this.slideText = UIStyle.INSTANCE.createTutorialSlideTextLabel(bodyText, FONT_COLOR);
        this.slideText.setWrap(true);
        this.slideText.setAlignment(Align.topLeft);
        this.slideText.setWidth(this.stage_width * 9f / 20f);
        this.slideText.setPosition(this.stage_width / 40f,
                                   this.slideHeader.getY(),
                                   Align.topLeft);

        // Add actors
        stage.addActor(header);
        stage.addActor(slideHeader);
        stage.addActor(slideText);
    }

    @Override
    public void hide() {
        slideImage.dispose();
        stage.dispose();
        super.dispose();
    }
} // End class
