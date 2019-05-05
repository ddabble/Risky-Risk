package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.view.PhaseView;

public class UIInputProcessor implements InputProcessor {

    private GameController gameController;
    private PhaseView phaseView;
    private OrthographicCamera camera;
    public UIInputProcessor(PhaseView phaseView, OrthographicCamera camera, GameController gameController){
        this.phaseView = phaseView;
        this.camera = camera;
        this.gameController = gameController;
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
        if (screenX > 0 && screenX < 100 && screenY > 0 && screenX < 200){
            gameController.nextPhaseButtonClicked();
            return true;
        }
        return false;
    }
}
