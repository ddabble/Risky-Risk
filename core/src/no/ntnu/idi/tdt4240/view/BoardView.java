package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;

public class BoardView extends ApplicationAdapter {
    private final GameController controller;
    //public final BoardController controller;

    private OrthographicCamera camera;

    private SpriteBatch batch;
    public Sprite mapSprite;
    public BoardModel model;
    private ShaderProgram mapShader;

    public BoardView(OrthographicCamera camera, RiskyRisk game, GameController controller) {
        this.camera = camera;
        model = game.getGameModel().getBoardModel();
        this.controller = controller;
    }

    /**
     * Must be called after {@link no.ntnu.idi.tdt4240.model.BoardModel} has been initialized.
     */
    @Override
    public void create() {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map

        mapSprite = new Sprite(controller.getMapTexture());
//        mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);




    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false;
    }



    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        float[] playerColorLookup = controller.getPlayerColorLookup().getFloatArray();
        mapShader.setUniform3fv("playerColorLookup", playerColorLookup, 0, playerColorLookup.length);
        mapSprite.draw(batch);
        batch.end();


    }

    @Override
    public void dispose() {
        batch.dispose();
        mapShader.dispose();
    }
}
