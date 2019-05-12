package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;

public class PhaseView extends AbstractView implements PhaseObserver {
    private int buttonWidth;
    private int buttonHeight;
    private TextButton phaseButton;
    private TextButton cancelButton;
    private TextButton attackButton;
    private TextButton turnButton;
    private TextButton fortifyButton;
    private TextButton exitToMainMenuButton;
    private Label phaseLabel;
    private Label playerLabel;
    private Stage stage;
    private OrthographicCamera camera;

    private Vector2 lineFrom;
    private Vector2 lineTo;
    private boolean shouldDrawArrow = false;
    private SpriteBatch spriteBatch;
    private TextureRegion region;
    private Texture texture;
    private Sprite spriteArrowHead;

    public PhaseView(OrthographicCamera camera) {
        PhasePresenter.addObserver(this);
        this.camera = camera;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void create() {
        super.create();
        buttonWidth = Gdx.graphics.getWidth()/7;
        buttonHeight = Gdx.graphics.getHeight()/14;
        // For drawing and input handling
        stage = new Stage(new ScreenViewport());

        spriteBatch = new SpriteBatch();
        texture = new Texture("arrow-tip.png");
        region = new TextureRegion(texture, 0, 0, 50, 50);
        spriteArrowHead = new Sprite(texture);
        spriteArrowHead.setScale(0.5f);
        spriteArrowHead.setColor(0, 0, 0, 0.7f);
        //spriteArrowHead.setSize(25,25); //scale the image down to 50%
        //spriteArrowHead.setOriginCenter();

        // Actors
        phaseLabel = createLabel("");
        phaseLabel.setPosition(0, 3*buttonHeight + 120);
        playerLabel = createLabel("");
        playerLabel.setPosition(0, 3*buttonHeight + 90);

        defineAllButtons();

        stage.addActor(phaseLabel);
        stage.addActor(playerLabel);
        stage.addActor(phaseButton);
        stage.addActor(exitToMainMenuButton);
    }

    /**
     * Define button for later use, but do not show them
     */
    private void defineAllButtons(){
        attackButton = createButton("Attack");
        attackButton.setWidth(buttonWidth);
        attackButton.setHeight(buttonHeight);
        attackButton.setPosition(0, 2*buttonHeight + 60);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.attackButtonClicked();
            }
        });

        fortifyButton = createButton("Move 1 Troop");
        fortifyButton.setWidth(buttonWidth);
        fortifyButton.setHeight(buttonHeight);
        fortifyButton.setPosition(0, 2*buttonHeight + 60);
        fortifyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.fortifyButtonClicked();
            }
        });

        cancelButton = createButton("Cancel Move");
        cancelButton.setWidth(buttonWidth);
        cancelButton.setHeight(buttonHeight);
        cancelButton.setPosition(0, buttonHeight + 30);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.cancelButtonClicked();
            }
        });

        turnButton = createButton("End Turn");
        turnButton.setWidth(buttonWidth);
        turnButton.setHeight(buttonHeight);
        turnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.nextTurnButtonClicked();
            }
        });

        phaseButton = createButton("");
        phaseButton.setWidth(buttonWidth);
        phaseButton.setHeight(buttonHeight);
        phaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.nextPhaseButtonClicked();
            }
        });

        exitToMainMenuButton = createInGameButton("Exit to Main Menu");
        //exitToMainMenuButton.setWidth(150);
        exitToMainMenuButton.setWidth(buttonWidth);
        exitToMainMenuButton.setHeight(buttonHeight);
        exitToMainMenuButton.setPosition(Gdx.graphics.getWidth()-buttonHeight, 0);
        exitToMainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.exitToMainMenuButtonClicked();
            }
        });
    }

    public void addActor(Actor actor){
        if (!stage.getActors().contains(actor, false))
            stage.addActor(actor);
    }
    public void removeActor(Actor actor){
        if (stage.getActors().contains(actor, false))
            actor.remove();
    }

    @Override
    public void addFortifyButton() {
        addActor(fortifyButton);
    }

    @Override
    public void addCancelButton() {
        addActor(cancelButton);
    }

    @Override
    public void addAttackButton() {
        addActor(attackButton);
    }

    @Override
    public void addTurnButton() { //TODO: change to better name
        removeActor(phaseButton);
        addActor(turnButton);
    }


    @Override
    public void removeTurnButton() { //TODO: change to better name
        removeActor(turnButton);
        addActor(phaseButton);
    }

    @Override
    public void removePhaseButtons() {
        removeActor(attackButton);
        removeActor(cancelButton);
        removeActor(fortifyButton);
    }

    @Override
    public void updateRenderedVariables(String phase, int troopsToPlace) {
        phaseLabel.setText("Current Phase: " + phase + " " +
                           "\nNumber of Troops to place: " + troopsToPlace);
    }

    @Override
    public void onNextPlayer(int playerID, Color playerColor) {
        playerLabel.setText("Player" + playerID);
        playerLabel.setStyle(new Label.LabelStyle(new BitmapFont(), playerColor));
    }

    @Override
    public void onNextPhase(String curPhase, String nextPhase) {
        phaseLabel.setText("Current Phase: " + curPhase);
        phaseButton.setText(nextPhase);
    }

    @Override
    public void onSelectedTerritoriesChange(Territory start, Territory end) {
        if (start != null && end != null) {
            lineFrom = start.getTroopCircleVector();
            lineTo = end.getTroopCircleVector();
            shouldDrawArrow = true;
        } else {
            shouldDrawArrow = false;
        }
    }

    @Override
    public void render() {
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Draw and update
        stage.act(); // Updates all actors
        stage.draw();

        if (shouldDrawArrow) {
            drawArrow(lineFrom, lineTo);
        }
    }

    private void drawArrow(Vector2 start, Vector2 end) {
        drawLine(start, end);
        // draw arrow head at end vector

        spriteBatch.enableBlending();
        spriteBatch.begin();
        //spriteBatch.draw(region, end.x, end.y);
        //rotate head
        float angle = new Vector2(end).sub(start).angle();
        spriteArrowHead.setRotation(angle - 90f);
        spriteArrowHead.setOriginBasedPosition(end.x, end.y); // center the sprite at (x, y)
        spriteArrowHead.draw(spriteBatch);
        spriteBatch.end();
    }

    private void drawLine(Vector2 start, Vector2 end) {
        Gdx.gl.glLineWidth(4);

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.line(start, end);
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1); //set back to default
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}
