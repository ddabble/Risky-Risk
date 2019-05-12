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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.presenter.TutorialPresenter;
import no.ntnu.idi.tdt4240.observer.TutorialObserver;

public class TutorialView extends AbstractView implements TutorialObserver, Screen {

    private final RiskyRisk game;
    private Stage stage;


    private int stage_width;
    private int stage_height;
    private int currentSlideCounter = 0;

    private BitmapFont headerFont;
    private BitmapFont slideHeaderFont;
    private BitmapFont slideTextFont;

    private TextField header;
    private TextField slideHeader;
    private TextField slideText;

    private Button previousButton;
    private Button nextButton;
    private Button mainMenuButton;

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
        Gdx.gl.glClearColor(233/255f,230/255f,185/255f,1);

        int imageWidth = this.stage_width/2 - this.stage_width/20;

        stage.getBatch().begin();
        stage.getBatch().draw(this.slideImage, this.stage_width-(imageWidth + this.stage_width/40), this.stage_height-420,imageWidth,300);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();


    }
    @Override
    public void show(){
        super.create();
        TutorialPresenter.INSTANCE.init();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.slideImage = new Texture(this.tutorialSlides.get(0).get("image"));

        this.stage_width = Gdx.graphics.getWidth();
        this.stage_height = Gdx.graphics.getHeight();


        this.createBitmapFonts();
        this.createButtons(stage);
        this.createTextFields(stage);
    }

    @Override
    public void hide(){
        this.slideImage.dispose();
        stage.dispose();
        super.dispose();
    }
    @Override
    public void resize(int i, int i1) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }


    @Override
    public void dispose() {
    }

    private void createButtons(Stage stage){

        // Button sizes
        int btnHeight = 50;
        int btnWidth = 100;

        // Main menu button
        this.mainMenuButton = this.createButton("Back to main");
        this.mainMenuButton.setPosition(this.stage_width/100, this.stage_height/100);
        this.mainMenuButton.setSize(btnWidth,btnHeight);

        // Previous button
        this.previousButton = this.createButton("Previous");
        this.previousButton.setPosition(this.stage_width - 2 * (btnWidth + this.stage_width/100), this.stage_height/100);
        this.previousButton.setSize(btnWidth,btnHeight);
        this.previousButton.setTouchable(Touchable.disabled);

        // Next button
        this.nextButton = this.createButton("Next");
        this.nextButton.setPosition(this.stage_width - (btnWidth + this.stage_width/100), this.stage_height/100);
        this.nextButton.setSize(btnWidth,btnHeight);


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


    private void createTextFields(Stage stage){

        Color fontColor = new Color(10/255f,10/255f,10/255f,1);

        // Header style
        TextField.TextFieldStyle headerStyle = new TextField.TextFieldStyle();
        headerStyle.font = this.headerFont;
        headerStyle.fontColor = fontColor;

        // Slide header style
        TextField.TextFieldStyle slideHeaderStyle = new TextField.TextFieldStyle();
        slideHeaderStyle.font = this.slideHeaderFont;
        slideHeaderStyle.fontColor = fontColor;

        // Slide text style
        TextField.TextFieldStyle slideTextStyle = new TextField.TextFieldStyle();
        slideTextStyle.font = this.slideTextFont;
        slideTextStyle.fontColor = fontColor;

        // Text field dimensions
        int headerWidth = 200;
        int headerHeight = 50;

        int slideHeaderWidth = this.stage_width/2;
        int slideHeaderHeight = 50;

        int tutSlideWidth = this.stage_width/2 - this.stage_width/20;
        int tutSlideHeight = 400;

        this.currentSlideCounter = 0;

        // Header text
        this.header = new TextField("Tutorial", headerStyle);
        this.header.setPosition(this.stage_width/2-headerWidth/2,this.stage_height - (headerHeight + this.stage_height/100));
        this.header.setSize(headerWidth, headerHeight);

        // Slide header text
        this.slideHeader = new TextField(this.tutorialSlides.get(this.currentSlideCounter).get("title"), slideHeaderStyle);
        this.slideHeader.setPosition(this.stage_width/40,this.stage_height - 3*slideHeaderHeight + 30);
        this.slideHeader.setSize(slideHeaderWidth, slideHeaderHeight);

        // Tutorial slide text
        this.slideText = new TextArea(this.tutorialSlides.get(this.currentSlideCounter).get("text"), slideTextStyle);
        this.slideText.setPosition(this.stage_width/40,this.stage_height - (tutSlideHeight+120));
        slideText.setSize(tutSlideWidth, tutSlideHeight);

        // Add actors
        stage.addActor(header);
        stage.addActor(slideHeader);
        stage.addActor(slideText);
    }

    private void updateCurrentSlide(boolean increment){
        if(increment && this.currentSlideCounter < this.tutorialSlides.size()-1){
            this.currentSlideCounter += 1;
        }
        else if(!increment && this.currentSlideCounter > 0){
            this.currentSlideCounter -= 1;
        }
        this.updateButtonState();
    }

    private void updateButtonState(){
        if(this.currentSlideCounter == this.tutorialSlides.size()-1){
            this.nextButton.setTouchable(Touchable.disabled);
        }
        else if(this.currentSlideCounter == 0){
            this.previousButton.setTouchable(Touchable.disabled);
        }
        else{
            this.nextButton.setTouchable(Touchable.enabled);
            this.previousButton.setTouchable(Touchable.enabled);
        }
    }

    private void createBitmapFonts(){
        FreeTypeFontGenerator headerGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter headerParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        headerParameter.size = 40;
        this.headerFont = headerGenerator.generateFont(headerParameter);
        headerGenerator.dispose();

        FreeTypeFontGenerator slideHeaderGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter slideHeaderParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        slideHeaderParameter.size = 25;
        this.slideHeaderFont = slideHeaderGenerator.generateFont(slideHeaderParameter);
        slideHeaderGenerator.dispose();

        FreeTypeFontGenerator slideTextGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/open-sans/OpenSans-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter slideTextParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        slideTextParameter.size = 20;
        this.slideTextFont = slideTextGenerator.generateFont(slideTextParameter);
        slideTextGenerator.dispose();
    }

} // End class
