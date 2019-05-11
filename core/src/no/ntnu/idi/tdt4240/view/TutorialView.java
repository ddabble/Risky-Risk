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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;

import no.ntnu.idi.tdt4240.RiskyRisk;
//import no.ntnu.idi.tdt4240.view.AbstractView;

public class TutorialView extends AbstractView implements Screen {
    private final RiskyRisk game;
    private Stage stage;


    private int stage_width;
    private int stage_height;
    private int currentSlideCounter = 0;

    private TextField header;
    private TextField slideHeader;
    private TextField slideText;

    private ArrayList<String> slideTexts;
    private ArrayList<String> slideHeaders;


    public TutorialView(RiskyRisk game) {
        this.game = game;



    }

    @Override
    public void create() {


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
        Button mainMenuButton = this.createButton("Back to main");
        mainMenuButton.setPosition(this.stage_width/100, this.stage_height/100);
        mainMenuButton.setSize(btnWidth,btnHeight);

        // Previous button
        Button previousButton = this.createButton("Previous");
        previousButton.setPosition(this.stage_width - 2 * (btnWidth + this.stage_width/100), this.stage_height/100);
        previousButton.setSize(btnWidth,btnHeight);

        // Next button
        Button nextButton = this.createButton("Next");
        nextButton.setPosition(this.stage_width - (btnWidth + this.stage_width/100), this.stage_height/100);
        nextButton.setSize(btnWidth,btnHeight);


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
                slideHeader.setText(slideHeaders.get(currentSlideCounter));
                slideText.setText(slideTexts.get(currentSlideCounter));
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                updateCurrentSlide(true);
                slideHeader.setText(slideHeaders.get(currentSlideCounter));
                slideText.setText(slideTexts.get(currentSlideCounter));
            }
        });

        // Add buttons to stage
        stage.addActor(mainMenuButton);
        stage.addActor(previousButton);
        stage.addActor(nextButton);

    } // End createButtons()


    private void createTextFields(Stage stage){

        // Test array list for slide text
        this.slideTexts = new ArrayList<>();
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

        // Bitmap font
        BitmapFont textFont = new BitmapFont();
        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = textFont;
        textStyle.fontColor = new Color(10/255f,10/255f,10/255f,1);

        // Header text
        int headerWidth = 88;
        int headerHeight = 50;
        this.header = new TextField("Tutorial", textStyle);
        this.header.setPosition(this.stage_width/2-headerWidth/2,this.stage_height - (headerHeight + this.stage_height/100));
        this.header.setSize(headerWidth, headerHeight);

        // Header text
        int slideHeaderWidth = 100;
        int slideHeaderHeight = 50;
        this.slideHeader = new TextField(this.slideHeaders.get(0), textStyle);
        this.slideHeader.setPosition(this.stage_width/2-slideHeaderWidth/2,this.stage_height - 3*(slideHeaderHeight + this.stage_height/100));
        this.slideHeader.setSize(slideHeaderWidth, slideHeaderHeight);

        // Tutorial slide text
        int tutSlideWidth = 400;
        int tutSlideHeight = 300;
        this.slideText = new TextArea(this.slideTexts.get(0), textStyle);
        this.slideText.setPosition(this.stage_width/2-tutSlideWidth/2,(this.stage_height - tutSlideHeight)/2);
        slideText.setSize(tutSlideWidth, tutSlideHeight);

        // Add actors
        stage.addActor(header);
        stage.addActor(slideHeader);
        stage.addActor(slideText);
    }

    private void updateCurrentSlide(boolean increment){
        if(increment && this.currentSlideCounter < this.slideTexts.size()-1){
            this.currentSlideCounter += 1;
        }
        else if(!increment && this.currentSlideCounter > 0){
            this.currentSlideCounter -= 1;
        }
    }

} // End class
