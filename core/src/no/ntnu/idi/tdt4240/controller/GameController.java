package no.ntnu.idi.tdt4240.controller;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;
import no.ntnu.idi.tdt4240.view.GameView;

public class GameController {
    private final GameView view;
    private final GameModel model;
    private final BoardModel boardModel;
    private final PhaseModel phaseModel;

    public GameController(GameView view, GameModel model) {
        this.view = view;
        this.model = model;
        this.boardModel = model.getBoardModel();
        this.phaseModel = model.getPhaseModel();
        this.initializeBoard();
    }

    public void initializeBoard() {

        Sprite mapSprite = new Sprite(boardModel.getMapTexture());
        float[] playerColorLookup = boardModel.getPlayerColorLookup().getFloatArray();

        view.setMapSprite(mapSprite);
        view.setPlayerColorLookup(playerColorLookup);

        List<Territory> territories = boardModel.TERRITORY_MAP.getAllTerritories();
        if (territories != null) {
            view.initializeBoard(territories);
        }

        this.updatePhase();
    }

    public void nextPhaseButtonClicked() {
        phaseModel.nextPhase();
        this.updatePhase();
    }

    public void updatePhase() {
        String curPhase = this.phaseModel.getPhase().getName();
        String nextPhase = this.phaseModel.getPhase().next().getName();
        view.updatePhase(curPhase, nextPhase);
    }

    public void boardClicked(Vector2 touchWorldPos) {
        Sprite mapSprite = boardModel.getMapSprite();
        //If this is a valid touch
        if (mapSprite.getBoundingRectangle().contains(touchWorldPos)) {
            model.onClickTerritory(touchWorldPos);

            // Update the view by getting changes from the model
            Territory territory = model.getSelectedTerritory();
            view.territorySelected(territory);
            if (territory != null) {
                view.updateTerritoryTroops(territory);
            }
        }
    }

    /*
    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        view.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    */
}
