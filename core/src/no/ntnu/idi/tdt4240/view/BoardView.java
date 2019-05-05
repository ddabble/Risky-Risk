package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.Map;

import no.ntnu.idi.tdt4240.RiskyRisk;
import no.ntnu.idi.tdt4240.model.BoardModel;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;

public class BoardView extends ApplicationAdapter {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    public Sprite mapSprite;
    public BoardModel model;
    private ShaderProgram mapShader;
    public float[] playerColorLookup;

    public BoardView(OrthographicCamera camera, RiskyRisk game) {
        this.camera = camera;
        model = game.getGameModel().getBoardModel();
    }

    /**
     * Must be called after {@link no.ntnu.idi.tdt4240.model.BoardModel} has been initialized.
     */
    @Override
    public void create() {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map
//      mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);

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
