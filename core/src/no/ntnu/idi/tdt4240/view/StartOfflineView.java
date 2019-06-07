package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.model.SettingsModel;
import no.ntnu.idi.tdt4240.view.data.UIStyle;

public class StartOfflineView extends ScreenAdapter {
    private static final Color FONT_COLOR = new Color(0x0A0A0AFF);

    private final RiskyRisk game;
    private final IGPGSClient gpgsClient;

    private Button decreaseButton;
    private Button increaseButton;
    private Stage stage;

    private int numPlayersChosen;

    public StartOfflineView(RiskyRisk game, IGPGSClient gpgsClient) {
        this.game = game;
        this.gpgsClient = gpgsClient;
    }

    @Override
    public void show() {
        stage = new Stage();
        // TODO: inject these values (more usages below) from a presenter; do not get them directly from the model
        numPlayersChosen = SettingsModel.MIN_NUM_PLAYERS;
        createLabelsAndButtons();
        MainMenuView.setInputProcessors_mainMenuSubViews(stage, game);

        Color backgroundColor = MainMenuView.BACKGROUND_COLOR;
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
    }

    private void createLabelsAndButtons() {
        final int screenWidth = Gdx.graphics.getWidth();
        final int screenHeight = Gdx.graphics.getHeight();
        final Vector2 screenCenter = new Vector2(screenWidth / 2f, screenHeight / 2f);
        final float middleButtonsHeight = screenHeight * 3f / 5f;

        final Label title = UIStyle.INSTANCE.createTutorialHeaderLabel("Choose number of players", FONT_COLOR);
        title.setPosition(screenCenter.x, screenHeight * 4f / 5f, Align.center);

        final Label numPlayersLabel = UIStyle.INSTANCE.createTutorialSlideHeaderLabel("0", FONT_COLOR); // temporary text value
        numPlayersLabel.setPosition(screenCenter.x, middleButtonsHeight, Align.center);

        final float buttonSize = numPlayersLabel.getHeight() * 2f;

        final Button startButton = UIStyle.INSTANCE.createTextButton("Start!");
        startButton.setSize(screenWidth / 5.5f, buttonSize);
        startButton.setPosition(screenCenter.x, screenHeight * 2f / 5f, Align.top);

        decreaseButton = UIStyle.INSTANCE.createTextButton("-");
        decreaseButton.setSize(buttonSize, buttonSize);
        decreaseButton.setPosition(startButton.getX(), middleButtonsHeight, Align.center);

        increaseButton = UIStyle.INSTANCE.createTextButton("+");
        increaseButton.setSize(buttonSize, buttonSize);
        increaseButton.setPosition(startButton.getRight(), middleButtonsHeight, Align.center);

        updateButtonState();
        setButtonListeners(startButton, numPlayersLabel);

        stage.addActor(title);
        stage.addActor(numPlayersLabel);
        stage.addActor(startButton);
        stage.addActor(decreaseButton);
        stage.addActor(increaseButton);
    }

    private void setButtonListeners(final Button startButton, final Label numPlayersLabel) {
        numPlayersLabel.setText(numPlayersChosen);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gpgsClient.onStartOfflineMatchClicked(numPlayersChosen);
                game.setScreen(RiskyRisk.ScreenEnum.GAME);
            }
        });

        decreaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (numPlayersChosen > SettingsModel.MIN_NUM_PLAYERS) {
                    numPlayersChosen--;
                    numPlayersLabel.setText(numPlayersChosen);
                }
                updateButtonState();
            }
        });

        increaseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (numPlayersChosen < SettingsModel.MAX_NUM_PLAYERS) {
                    numPlayersChosen++;
                    numPlayersLabel.setText(numPlayersChosen);
                }
                updateButtonState();
            }
        });
    }

    private void updateButtonState() {
        if (numPlayersChosen == SettingsModel.MIN_NUM_PLAYERS)
            decreaseButton.setTouchable(Touchable.disabled);
        else if (numPlayersChosen == SettingsModel.MAX_NUM_PLAYERS)
            increaseButton.setTouchable(Touchable.disabled);
        else {
            decreaseButton.setTouchable(Touchable.enabled);
            increaseButton.setTouchable(Touchable.enabled);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void hide() {
        stage.dispose();
    }
}
