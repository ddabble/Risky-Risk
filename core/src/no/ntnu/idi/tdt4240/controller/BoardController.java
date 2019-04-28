package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.model.Territory;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;
import no.ntnu.idi.tdt4240.view.BoardView;

public class BoardController {

    // TODO: make this controller actually do something

    private BoardModel model;
    private BoardView view;

    public BoardController(BoardModel model, BoardView view) {
        this.model = model;
        this.view = view;
    }

    public BoardModel getModel() {
        return model;
    }

    public BoardView getView() {
        return view;
    }

    public ColorArray getPlayerColorLookup() {
        return model.getPlayerColorLookup();
    }

    public Texture getMapTexture() {
        return model.getMapTexture();
    }

    public Vector2 worldPosToMapTexturePos(Vector2 worldPos, Sprite mapSprite) {
        return model.worldPosToMapTexturePos(worldPos, mapSprite);
    }

    public Territory getTerritory(Vector2 mapPos) {
        return model.getTerritory(mapPos);
    }
}
