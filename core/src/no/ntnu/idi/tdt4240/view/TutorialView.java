package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
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

public class TutorialView extends ScreenAdapter implements TutorialObserver {
    private static final Color FONT_COLOR = new Color(0x0A0A0AFF);

    private final RiskyRisk game;

    private Stage stage;

    private int stageWidth;
    private int stageHeight;
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

        // Render the tutorial slide image
        int imageWidth = this.stageWidth / 2 - this.stageWidth / 20;
        int imageHeight = 2 * imageWidth / 3;
        stage.getBatch().begin();
        stage.getBatch().draw(this.slideImage,
                              this.stageWidth - (imageWidth + this.stageWidth / 40f),
                              this.slideHeader.getTop() - imageHeight,
                              imageWidth, imageHeight);
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {
        TutorialPresenter.init();
        stage = new Stage(new ScreenViewport());
        MainMenuView.setInputProcessors_mainMenuSubViews(stage, game);

        this.slideImage = new Texture(this.tutorialSlides.get(0).get("image"));

        this.stageWidth = Gdx.graphics.getWidth();
        this.stageHeight = Gdx.graphics.getHeight();

        this.createButtons(stage);
        this.createTextFields(stage);

        Color backgroundColor = MainMenuView.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
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
        final float btnHeight = stageHeight / 11f;
        final float btnWidth = stageWidth / 5.5f;

        Color fontColor = new Color(Color.BLACK);

        // Main menu button
        this.mainMenuButton = UIStyle.INSTANCE.createTutorialButton("Back to Main Menu", fontColor);
        this.mainMenuButton.setPosition(this.stageWidth / 100f,
                                        this.stageHeight / 50f);
        this.mainMenuButton.setSize(btnWidth * 1.4f, btnHeight);

        // Previous button
        this.previousButton = UIStyle.INSTANCE.createTutorialButton("Previous", fontColor);
        this.previousButton.setPosition(this.stageWidth - 2 * (btnWidth + this.stageWidth / 100f),
                                        this.stageHeight / 50f);
        this.previousButton.setSize(btnWidth, btnHeight);
        this.previousButton.setTouchable(Touchable.disabled);

        // Next button
        this.nextButton = UIStyle.INSTANCE.createTutorialButton("Next", fontColor);
        this.nextButton.setPosition(this.stageWidth - (btnWidth + this.stageWidth / 100f),
                                    this.stageHeight / 50f);
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
        this.header.setPosition(this.stageWidth / 2f,
                                this.stageHeight * 29f / 30f,
                                Align.top);

        // Slide header text
        String headerText = this.tutorialSlides.get(this.currentSlideCounter).get("title");
        this.slideHeader = UIStyle.INSTANCE.createTutorialSlideHeaderLabel(headerText, FONT_COLOR);
        this.slideHeader.setAlignment(Align.left);
        this.slideHeader.setWidth(this.stageWidth / 2f);
        this.slideHeader.setPosition(this.stageWidth / 40f,
                                     this.header.getY() - this.slideHeader.getHeight() / 2f,
                                     Align.topLeft);

        // Tutorial slide text
        String bodyText = this.tutorialSlides.get(this.currentSlideCounter).get("text");
        this.slideText = UIStyle.INSTANCE.createTutorialSlideTextLabel(bodyText, FONT_COLOR);
        this.slideText.setWrap(true);
        this.slideText.setAlignment(Align.topLeft);
        this.slideText.setWidth(this.stageWidth * 9f / 20f);
        this.slideText.setPosition(this.stageWidth / 40f,
                                   this.slideHeader.getY(),
                                   Align.topLeft);

        // Add actors
        stage.addActor(header);
        stage.addActor(slideHeader);
        stage.addActor(slideText);
    }

    @Override
    public void hide() {
        Gdx.input.setCatchBackKey(false);

        slideImage.dispose();
        stage.dispose();
        super.dispose();
    }
} // End class
