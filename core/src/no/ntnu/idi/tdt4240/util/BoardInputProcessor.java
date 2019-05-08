package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.view.BoardView;

public class BoardInputProcessor implements InputProcessor {
    private final GameController gameController;
    private final BoardView view;
    private OrthographicCamera camera;

    public BoardInputProcessor(GameController gameController, BoardView view, OrthographicCamera camera) {
        this.gameController = gameController;
        this.view = view;
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

        // This is related to UI, so we keep this in here
        Vector3 _touchWorldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        Vector2 touchWorldPos = new Vector2(_touchWorldPos.x, _touchWorldPos.y);
        if (!view.isPosWithinMap(touchWorldPos))
            return false;

        gameController.boardClicked(touchWorldPos);
        return true;
    }
}
