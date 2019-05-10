package no.ntnu.idi.tdt4240.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.xml.soap.Text;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.PhaseController;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.controller.GameController;


public class PhaseView extends AbstractView {

    private final GameController gameController;

    private TextButton phaseButton;
    private TextButton cancelButton;
    private TextButton attackButton;
    private TextButton turnButton;
    private TextButton fortifyButton;
    private Label phaseLabel;
    private Label playerLabel;
    private Stage stage;
    private static OrthographicCamera camera;
    private PhaseController controller;

    private Vector2 lineFrom;
    private Vector2 lineTo;
    private boolean shouldDrawArrow = false;
    private SpriteBatch spriteBatch;
    private TextureRegion region;
    private Texture texture;
    private Sprite spriteArrowHead;

    // Renderers


    public PhaseView(RiskyRisk game, GameController gameController, OrthographicCamera camera) {
        super(game);
        this.gameController = gameController;
        this.camera = camera;
    }

    public Stage getStage() {
        return stage;
    }

    public void updatePhase(String curPhase, String nextPhase) {
        phaseLabel.setText("Current Phase: " + curPhase);
        phaseButton.setText(nextPhase);
    }


    public void addFortifyButton(){
        if (!stage.getActors().contains(fortifyButton, false)) {
            fortifyButton = createButton("Move 1 Troop");
            fortifyButton.setWidth(100);
            fortifyButton.setPosition(0, 60);
            fortifyButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameController.fortifyButtonClicked();
                }
            });
            stage.addActor(fortifyButton);
        }
    }

    public void addCancelButton(){
        if (!stage.getActors().contains(cancelButton, false)){
            cancelButton = createButton("Cancel Move");
            cancelButton.setWidth(100);
            cancelButton.setPosition(0, 30);
            cancelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameController.cancelButtonClicked();
                }
            });
            stage.addActor(cancelButton);
        }

    }
    public void addAttackButton(){
        if (!stage.getActors().contains(attackButton, false)) {
            attackButton = createButton("Attack");
            attackButton.setWidth(100);
            attackButton.setPosition(0, 60);
            attackButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    gameController.attackButtonClicked();
                }
            });
            stage.addActor(attackButton);
        }
    }

    public void addTurnButton(){
        phaseButton.remove();
        turnButton = createButton("End Turn");
        turnButton.setWidth(100);
        turnButton.setPosition(0, 0);
        turnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameController.nextTurnButtonClicked();
            }
        });
        stage.addActor(turnButton);
    }

    public void removeTurnButton(){

        turnButton.remove();
        phaseButton = createButton("");
        phaseButton.setWidth(100);
        phaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameController.nextPhaseButtonClicked();
            }
        });
        stage.addActor(phaseButton);
    }

    public void removeFortifyButton(){
        if (stage.getActors().contains(fortifyButton, false))
            fortifyButton.remove();
    }
    public void removeCancelButton(){
        if (stage.getActors().contains(cancelButton, false))
            cancelButton.remove();

    }
    public void removeAttackButton(){
        if (stage.getActors().contains(attackButton, false))
            attackButton.remove();
    }

    public void updateRenderedVariables(String phase){
        phaseLabel.setText("Current Phase: " + phase + " " +
                "\nNumber of Troops to place: " + gameController.getPlayer().getTroopsToPlace());
    }

    public void updateRenderedCurrentPlayer(int playerID, Color playerColor){
        playerLabel.setText("Player" + playerID);
        playerLabel.setStyle(new Label.LabelStyle(new BitmapFont(), playerColor));
    }

    @Override
    public void show() {
        super.show();

        // For drawing and input handling
        stage = new Stage(new ScreenViewport());

        spriteBatch = new SpriteBatch();
        texture = new Texture("arrow-tip.png");
        region = new TextureRegion(texture, 0, 0, 50, 50);
        spriteArrowHead = new Sprite(texture);
        spriteArrowHead.setScale(0.5f);
        //spriteArrowHead.setSize(25,25); //scale the image down to 50%
        //spriteArrowHead.setOriginCenter();

        // Actors
        phaseLabel = createLabel("");
        phaseLabel.setPosition(0, 200);
        playerLabel = createLabel("");
        playerLabel.setPosition(0, 105);
        phaseButton = createButton("");
        phaseButton.setWidth(100);
        phaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameController.nextPhaseButtonClicked();
            }
        });


        stage.addActor(phaseLabel);
        stage.addActor(playerLabel);
        stage.addActor(phaseButton);
    }


    @Override
    public void render(float delta) {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw and update

        stage.act(delta); // Updates all actors
        stage.draw();

        if(shouldDrawArrow){
            drawArrow(lineFrom,lineTo);
        }
    }

    public void onSelectedTerritoriesChange(Territory start, Territory end) {
        if (start != null && end != null){
            lineFrom = start.getTroopCircleVector();
            lineTo = end.getTroopCircleVector();
            shouldDrawArrow = true;
        } else {
            shouldDrawArrow = false;
        }
    }

    private void drawArrow(Vector2 start, Vector2 end){
        drawLine(start, end);
        // draw arrow head at end vector


        spriteBatch.enableBlending();
        spriteBatch.begin();
        //spriteBatch.draw(region, end.x, end.y);
        //rotate head
        float angle = new Vector2(end).sub(start).angle();
        spriteArrowHead.setRotation(angle-90f);
        spriteArrowHead.setOriginBasedPosition(end.x,end.y); // center the sprite at (x, y)
        spriteArrowHead.draw(spriteBatch);
        spriteBatch.end();
    }

    public void onMapMove() {
    }

    private void drawLine(Vector2 start, Vector2 end)
    {
        Gdx.gl.glLineWidth(4);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0,0,0, 0.02f);
        shapeRenderer.line(start, end);
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1); //set back to default
    }

    @Override
    public void hide() {
        stage.dispose();
        super.hide();
    }


}
