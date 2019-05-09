package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Map;

import no.ntnu.idi.tdt4240.controller.GameController;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;

public class BoardView extends ApplicationAdapter {
    private final GameController gameController;

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private Sprite mapSprite;

    private ShaderProgram mapShader;

    private final ColorArray PLAYER_COLOR_LOOKUP = new ColorArray(0xFF + 1, 3);

    public BoardView(GameController gameController, OrthographicCamera camera) {
        this.gameController = gameController;
        this.camera = camera;
    }

    public boolean isPosWithinMap(Vector2 pos) {
        return mapSprite.getBoundingRectangle().contains(pos);
    }

    public Vector2 worldPosToMapTexturePos(Vector2 worldPos) {
        Vector2 mapPos = new Vector2(worldPos).sub(mapSprite.getX(), mapSprite.getY());
        // Invert y coord, because the texture's origin is in the upper left corner
        mapPos.y = mapSprite.getHeight() - mapPos.y;
        // Round the coords, because it's needed for getting texture pixels
        mapPos.x = MathUtils.roundPositive(mapPos.x);
        mapPos.y = MathUtils.roundPositive(mapPos.y);
        return mapPos;
    }

    /**
     * Must be called after {@link no.ntnu.idi.tdt4240.model.BoardModel} has been initialized.
     */
    public void create(Texture mapTexture, TerritoryMap territoryMap) {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map

        mapSprite = new Sprite(mapTexture);
//      mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);

        initColorLookupArray(territoryMap);
    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false;
    }

    public void initColorLookupArray(TerritoryMap territoryMap) {
        for (Territory territory : territoryMap.getAllTerritories()) {
            int playerColor = gameController.getPlayerColor(territory.getOwnerID());
            PLAYER_COLOR_LOOKUP.setColor(territory.colorIndex, playerColor << 8);
        }
    }

    @Override
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float[] playerColorLookup = PLAYER_COLOR_LOOKUP.getFloatArray();
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
