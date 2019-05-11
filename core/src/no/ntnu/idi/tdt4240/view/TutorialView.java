package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.TutorialController;
import no.ntnu.idi.tdt4240.observer.TutorialObserver;
//import no.ntnu.idi.tdt4240.view.AbstractView;

public class TutorialView extends AbstractView implements TutorialObserver, Screen {
    private final RiskyRisk game;
    private Stage stage;


    private int stage_width;
    private int stage_height;
    private int currentSlideCounter = 0;

    private TextField header;
    private TextField slideHeader;
    private TextField slideText;

    private Button previousButton;
    private Button nextButton;
    private Button mainMenuButton;

    //private ArrayList<String> slideTexts;
    //private ArrayList<String> slideHeaders;
    private ArrayList<Map<String, String>> tutorialSlides;


    public TutorialView(RiskyRisk game) {
        this.game = game;
        TutorialController.addObserver(this);
    }


    @Override
    public void create(ArrayList<Map<String, String>> tutorialSlides) {
        System.out.println("hello");
        for (int i=0; i<tutorialSlides.size();i++){
            System.out.println(tutorialSlides.get(i));
        }

        this.tutorialSlides = tutorialSlides;

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(233/255f,230/255f,185/255f,1);
        stage.act(delta);
        stage.draw();


    }
    @Override
    public void show(){
        super.create();
        TutorialController.INSTANCE.init();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        this.stage_width = Gdx.graphics.getWidth();
        this.stage_height = Gdx.graphics.getHeight();

        this.createButtons(stage);
        this.createTextFields(stage);


    }
    @Override
    public void hide(){}
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

        // Back to menu button
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
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateCurrentSlide(true);
                slideHeader.setText(tutorialSlides.get(currentSlideCounter).get("title"));
                slideText.setText(tutorialSlides.get(currentSlideCounter).get("text"));
            }
        });

        // Add buttons to stage
        stage.addActor(mainMenuButton);
        stage.addActor(previousButton);
        stage.addActor(nextButton);

    } // End createButtons()


    private void createTextFields(Stage stage){

        // Test array list for slide text
        /*this.slideTexts = new ArrayList<>();
        this.slideTexts.add("Slide 1 sejfd sjfdsl seproj sd sodk sdøok sødok sd dsofgsdøfogksd" +
                "dsfgjsdkfjgls s fgj slkfjlsdkfjg sdfljgslrij  rogjer oøgkoøsdg fgsdoøfgk øe" +
                "fdkgmsdlfgsø fdsø sdføl ksdføl ksdølf ksødl ksdølf ksd.");
        this.slideTexts.add("Slide 2 sejfd sjfdsl seproj sd sodk sdøok sødok sd dsofgsdøfogksd" +
                "sdfgksdøf sfd øosdkø gksdfø ksdølf gsdølf gksdøl." +
                "dsfgjsdkfjgls s fgj slkfjlsdkfjg sdfljgslrij  rogjer oøgkoøsdg fgsdoøfgk øe");
        this.slideTexts.add("Slide 3 sejfd sjfdsl seproj sd sodk sdøok sødok sd dsofgsdøfogksd" +
                "dsfgjsdkfjgls s fgj slkfjlsdkfjg sdfljgslrij  rogjer oøgkoøsdg fgsdoøfgk øe" +
                "edfn lskdfg sdfk sølfkg søldkfgsdølf ksdølf sdølf kgs " +
                "sdlf lsdøf kgsdøf ølsdk gsødlf ksølfdøsløsdlfk sdlf øsdfl ksdøfl gksdøf lgk.");

        // Test array list for slide header
        this.slideHeaders = new ArrayList<>();
        this.slideHeaders.add("Slide header 1");
        this.slideHeaders.add("Slide header 2");
        this.slideHeaders.add("Slide header 3");
*/
        // Bitmap font
        BitmapFont textFont = new BitmapFont();
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = textFont;
        textStyle.fontColor = new Color(10/255f,10/255f,10/255f,1);

        // Text field dimensions
        int headerWidth = 88;
        int headerHeight = 50;

        int slideHeaderWidth = 200;
        int slideHeaderHeight = 50;

        int tutSlideWidth = 400;
        int tutSlideHeight = 300;

        // Header text
        this.header = new TextField("Tutorial", textStyle);
        this.header.setPosition(this.stage_width/2-headerWidth/2,this.stage_height - (headerHeight + this.stage_height/100));
        this.header.setSize(headerWidth, headerHeight);

        // Slide header text
        this.slideHeader = new TextField(this.tutorialSlides.get(0).get("title"), textStyle);
        this.slideHeader.setPosition(this.stage_width/2-tutSlideWidth/2,this.stage_height - 3*(slideHeaderHeight + this.stage_height/200));
        this.slideHeader.setSize(slideHeaderWidth, slideHeaderHeight);

        // Tutorial slide text
        this.slideText = new TextArea(this.tutorialSlides.get(0).get("text"), textStyle);
        this.slideText.setPosition(this.stage_width/2-tutSlideWidth/2,(this.stage_height - tutSlideHeight)/2);
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

} // End class
