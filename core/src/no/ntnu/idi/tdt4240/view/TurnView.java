package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.controller.GameViewer;
import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.controller.IRiskyTurn;
import no.ntnu.idi.tdt4240.RiskyRisk;

public class TurnView extends AbstractView implements GameViewer{


    private final Button doneButton;
    private final Label turnCounter;
    private Stage stage;
    private Table table;
    private GameController gameController;
    private IGPGSClient gpgsClient;
    private final TextField dataToSend;
    private Label dataReceived;

    public TurnView(RiskyRisk game) {
        super(game);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setDebug(true);
        table.setFillParent(true);
        stage.addActor(table);

        gpgsClient = game.gpgsClient;
        gameController = new GameController(this, game.getGameModel());

        // Gameplay Layout
        turnCounter = this.createLabel("");
        dataReceived = this.createLabel("");
        dataToSend = this.createTextField("");

        table.add(turnCounter);
        table.add(dataReceived);
        table.add(dataToSend);
        table.row();

        doneButton = this.createButton("Done with turn");
        Button cancelButton = this.createButton("Cancel game");
        Button leaveButton = this.createButton("Leave game");
        Button finishButton = this.createButton("Finish game (debug)");

        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    String data = dataToSend.getText();
                    gpgsClient.onDoneClicked(data);
                }
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onCancelClicked();

                }
            }
        });

        leaveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onLeaveClicked();

                }
            }
        });

        finishButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gpgsClient != null) {
                    gpgsClient.onFinishClicked();

                }
            }
        });



        table.add(doneButton);
        table.row();
        table.add(cancelButton);
        table.add(leaveButton);
        table.add(finishButton);

        table.row();


    }

    @Override
    public void show() {
        Button backButton = this.createButton("Back to main");

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gpgsClient.pauseGame();
                game.setScreen(new MainMenuView(game));

            }
        });

        table.add(backButton);
    }

    @Override
    public void render(float delta) {

        if (!gpgsClient.matchActive()){
            game.setScreen(new MainMenuView(game));
        }

        if (gpgsClient.isDoingTurn()) {
            doneButton.setVisible(true);
            dataToSend.setVisible(true);
        } else {
            doneButton.setVisible(false);
            dataToSend.setVisible(false);

        }

        // Check if we need to update view
        IRiskyTurn turn = gpgsClient.getmRiskyTurn();
        if (turn != null) {

            int counter = turn.getTurnCounter();
            String receivedData = turn.getTurnData();

            turnCounter.setText(counter);
            dataReceived.setText(receivedData);
        }


        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        //stage.getBatch().draw(background, 0, 0);
        stage.getBatch().end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        this.dispose();
    }

    @Override
    public void dispose() {
        //img.dispose();
    }

    @Override
    public void setNumberOfPlayers(int num) {

    }
}
