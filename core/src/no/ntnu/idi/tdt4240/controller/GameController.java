package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.GameModel;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;

public class GameController {
    private final GameViewer viewer;
    private final GameModel model;
    private final BoardModel boardModel;

    public GameController(GameViewer viewer, GameModel model) {
        this.viewer = viewer;
        this.model = model;
        this.boardModel = model.getBoardModel();
    }

    public void setNumberOfPlayers(int num) {
        model.gameSettings.setNumberOfPlayers(num);
        viewer.setNumberOfPlayers(model.gameSettings.getNumberOfPlayers());
    }

    public int getNumberOfPlayers() {return model.gameSettings.getNumberOfPlayers();}

    public ColorArray getPlayerColorLookup() {
        return boardModel.getPlayerColorLookup();
    }

    public Texture getMapTexture() {
        return boardModel.getMapTexture();
    }

    public Vector2 worldPosToMapTexturePos(Vector2 worldPos, Sprite mapSprite) {
        return boardModel.worldPosToMapTexturePos(worldPos, mapSprite);
    }

    public Territory getTerritory(Vector2 mapPos) {
        return boardModel.getTerritory(mapPos);
    }

    // The role of the controller is to translate inputs into changes and relay this back
    // Below is the translation of clicks to model changes
    public void boardClicked(Vector2 touchWorldPos) {
        Sprite mapSprite = boardModel.getMapSprite();
        if (mapSprite.getBoundingRectangle().contains(touchWorldPos)) {
            Vector2 mapPos = worldPosToMapTexturePos(touchWorldPos, mapSprite);

            // TODO: this should be in the model since we do some checks...
            Territory territory = getTerritory(mapPos);
            if (territory != null) {
                System.out.println(territory.name);
                System.out.println("\tOwnerID: " + territory.getOwnerID());
                System.out.println("\tNumber of Troops: " + territory.getNumTroops());
                territory.setNumTroops(territory.getNumTroops() + 1);
                //TODO:... until here
                //the troopview needs to tell ... troopView.onTerritoryChangeNumTroops(territory);

                viewer.territorySelected(territory);//.onSelectTerritory(territory);
                viewer.updateTerritoryTroops(territory);
            } else {
                System.out.println("None");
                //troopView.onSelectTerritory(null);
            }
        }

        //TODO: update view
    }
}
