package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.view.BoardView;
import no.ntnu.idi.tdt4240.view.GameView;
import no.ntnu.idi.tdt4240.view.TroopView;

public class BoardInputProcessor implements InputProcessor {
    private BoardView boardView;
    private TroopView troopView;
    private OrthographicCamera camera;


    public BoardInputProcessor(BoardView boardView, TroopView troopView, OrthographicCamera camera){
        this.boardView = boardView;
        this.troopView = troopView;
        this.camera = camera;

    }
    public boolean keyDown (int keycode) {
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (int amount) {
        return false;
    }
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button != Input.Buttons.LEFT) // Only useful for desktop
            return false;

        Vector3 _touchWorldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        Vector2 touchWorldPos = new Vector2(_touchWorldPos.x, _touchWorldPos.y);
        if (boardView.mapSprite.getBoundingRectangle().contains(touchWorldPos)) {
            Vector2 mapPos = boardView.controller.worldPosToMapTexturePos(touchWorldPos, boardView.mapSprite);
            Territory territory = boardView.controller.getTerritory(mapPos);

            // TODO: Remove; the following is debugging code:
            if (territory != null) {
                System.out.println(territory.name);
                System.out.println("\tOwnerID: " + territory.getOwnerID());
                System.out.println("\tNumber of Troops: " + territory.getNumTroops());
                territory.setNumTroops(territory.getNumTroops() + 1);
                troopView.onTerritoryChangeNumTroops(territory);
                troopView.onSelectTerritory(territory);
            } else {
                System.out.println("None");
                troopView.onSelectTerritory(null);
            }
        }


        return true;
    }
}
