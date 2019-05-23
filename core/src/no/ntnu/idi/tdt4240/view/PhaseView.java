package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.observer.PhaseObserver;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;
import no.ntnu.idi.tdt4240.util.PhaseEnum;
import no.ntnu.idi.tdt4240.util.Utils;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class PhaseView extends ApplicationAdapter implements PhaseObserver {
    private static final float ARROW_HEAD_SIZE_MAP_RATIO = 1 / 30f;

    private final BoardView boardView;

    private OrthographicCamera camera;

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
    private Label waitingForTurnLabel;
    private Stage stage;

    private Vector2 lineFrom;
    private Vector2 lineTo;
    private boolean shouldDrawArrow;
    private SpriteBatch spriteBatch;
    private Texture texture;

    private ShapeRenderer shapeRenderer;
    private Sprite spriteArrowHead;

    public PhaseView(BoardView boardView) {
        PhasePresenter.addObserver(this);
        this.boardView = boardView;
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void create(OrthographicCamera camera) {
        this.camera = camera;
        shouldDrawArrow = false;

        buttonWidth = Math.round(Gdx.graphics.getWidth() / 6.3f);
        buttonHeight = Gdx.graphics.getHeight() / 13;
        // For drawing and input handling
        stage = new Stage(new ScreenViewport());

        spriteBatch = new SpriteBatch();
        texture = new Texture("arrow-tip.png");
        spriteArrowHead = new Sprite(texture);
        Utils.setSizeOfSprite(spriteArrowHead, ARROW_HEAD_SIZE_MAP_RATIO);
        spriteArrowHead.setColor(0, 0, 0, 0.7f);
        shapeRenderer = new ShapeRenderer();

        // Actors
        phaseLabel = UIStyle.INSTANCE.createInGameLabel("");
        //noinspection IntegerDivisionInFloatingPointContext
        phaseLabel.setPosition(Gdx.graphics.getWidth() / 2, buttonHeight);
        phaseLabel.setWidth(0);
        phaseLabel.setColor(Color.DARK_GRAY);

        playerLabel = UIStyle.INSTANCE.createPlayerColorableLabel("");
        playerLabel.setPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 20f);

        defineAllButtons();

        waitingForTurnLabel = UIStyle.INSTANCE.createLabel("Match object sent, wait for your turn...");
        waitingForTurnLabel.setPosition(150, 150);

        stage.addActor(phaseLabel);
        stage.addActor(playerLabel);
        stage.addActor(phaseButton);
        stage.addActor(exitToMainMenuButton);
    }

    private TextButton defineButton(String text, int x, int y) {
        TextButton b = UIStyle.INSTANCE.createTextButton(text);
        b.setWidth(buttonWidth);
        b.setHeight(buttonHeight);
        b.setPosition(x, y);
        return b;
    }

    /**
     * Define button for later use, but do not show them
     */
    private void defineAllButtons() {
        attackButton = defineButton("Attack", 0, 2 * buttonHeight + 20);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.attackButtonClicked();
            }
        });

        fortifyButton = defineButton("Move 1 troop", 0, 2 * buttonHeight + 20);
        fortifyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.fortifyButtonClicked();
            }
        });

        cancelButton = defineButton("Cancel", 0, buttonHeight + 10);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.cancelButtonClicked();
            }
        });

        turnButton = defineButton("End turn", 0, 0);
        turnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.nextTurnButtonClicked();
            }
        });

        phaseButton = defineButton("", 0, 0);
        phaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.nextPhaseButtonClicked();
            }
        });

        exitToMainMenuButton = UIStyle.INSTANCE.createTextButton("Exit to Main Menu");
        exitToMainMenuButton.setWidth((int)Math.round(buttonWidth * 1.5));
        exitToMainMenuButton.setHeight(buttonHeight);
        exitToMainMenuButton.setPosition(Gdx.graphics.getWidth() - exitToMainMenuButton.getWidth(), 0);
        exitToMainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                PhasePresenter.INSTANCE.exitToMainMenuButtonClicked();
            }
        });
    }

    private void addActor(Actor actor) {
        if (!stage.getActors().contains(actor, false))
            stage.addActor(actor);
    }

    private void removeActor(Actor actor) {
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
    public void onMapRenderingChanged() {
        spriteArrowHead.setScale(camera.zoom);
    }

    @Override
    public void onNextPlayer(int playerID, Color playerColor) {
        playerLabel.setText("Player" + playerID + "'s turn");
        playerLabel.setColor(playerColor);
    }

    @Override
    public void onNextPhase(PhaseEnum curPhase, PhaseEnum nextPhase) {
        phaseLabel.setText("Current Phase: " + curPhase);
        phaseButton.setText(nextPhase.toString() + " phase");
    }

    @Override
    public void onSelectedTerritoriesChange(Territory start, Territory end) {
        if (start != null && end != null) {
            lineFrom = Utils.screenToWorldPos(boardView.screenPosRelativeToMap(start.getTroopCircleVector()), camera);
            lineTo = Utils.screenToWorldPos(boardView.screenPosRelativeToMap(end.getTroopCircleVector()), camera);
            shouldDrawArrow = true;
        } else {
            shouldDrawArrow = false;
        }
    }

    @Override
    public void onWaitingForTurn() {
        //dummy code, just to give some indication
        //that the match was actually sent
        turnButton.remove();
        stage.addActor(waitingForTurnLabel);
    }

    @Override
    public void render() {
        // Draw and update
        if (shouldDrawArrow)
            drawArrow(lineFrom, lineTo);

        stage.act(); // Updates all actors
        stage.draw();
    }

    private void drawArrow(Vector2 start, Vector2 end) {
        drawLine(start, end);
        // draw arrow head at end vector

        spriteBatch.enableBlending();
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        //rotate head
        float angle = end.cpy().sub(start).angle();
        spriteArrowHead.setRotation(angle - 90f);
        spriteArrowHead.setOriginBasedPosition(end.x, end.y); // center the sprite at (x, y)
        spriteArrowHead.draw(spriteBatch);
        spriteBatch.end();
    }

    private void drawLine(Vector2 start, Vector2 end) {
        Gdx.gl.glLineWidth(4);
        Gdx.gl.glEnable(GL20.GL_BLEND); //make it work when debug mode is off
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA); //make it work when debug mode is off
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0, 0, 0, 0.7f);
        shapeRenderer.line(start, end);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND); //make it work when debug mode is off
        Gdx.gl.glLineWidth(1); //set back to default
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        stage.dispose();
        spriteBatch.dispose();
        texture.dispose();
        super.dispose();
    }
}
