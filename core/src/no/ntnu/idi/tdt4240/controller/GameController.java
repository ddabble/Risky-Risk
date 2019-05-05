package no.ntnu.idi.tdt4240.controller;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.model.PhaseModel;

public class GameController {
    private final GameViewer viewer;
    private final GameModel model;
    private final BoardModel boardModel;

    public GameController(GameViewer viewer, GameModel model) {
        this.viewer = viewer;
        this.model = model;

        this.boardModel = model.getBoardModel();
        this.initializeBoard();
    }

    public void initializeBoard() {

        Sprite mapSprite = new Sprite(boardModel.getMapTexture());
        float[] playerColorLookup = boardModel.getPlayerColorLookup().getFloatArray();

        viewer.setMapSprite(mapSprite);
        viewer.setPlayerColorLookup(playerColorLookup);

        List<Territory> territories = boardModel.TERRITORY_MAP.getAllTerritories();
        if (territories != null) {
            viewer.initializeBoard(territories);
        }
    }

    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        viewer.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    public void boardClicked(Vector2 touchWorldPos) {
        Sprite mapSprite = boardModel.getMapSprite();
        //If this is a valid touch
        if (mapSprite.getBoundingRectangle().contains(touchWorldPos)) {
            model.onClickTerritory(touchWorldPos);

            // Update the view by getting changes from the model
            Territory territory = model.getSelectedTerritory();
            viewer.territorySelected(territory);
            if (territory != null) {
                viewer.updateTerritoryTroops(territory);
            }
        }
    }
}
