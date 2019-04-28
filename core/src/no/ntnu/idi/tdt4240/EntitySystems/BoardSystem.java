package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import no.ntnu.idi.tdt4240.model.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;


public class BoardSystem extends ApplicationAdapter {
    private TerritoryMap territoryMap;

    private final ColorArray PLAYER_COLOR_LOOKUP = new ColorArray(0xFF + 1, 3);

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture mapTexture;
    private Pixmap mapPixmap;
    private Sprite mapSprite;

    private ShaderProgram mapShader;

    public BoardSystem(OrthographicCamera camera) {
        super();
        this.camera = camera;

        territoryMap = TerritoryMap.parseJsonMapStructure(Gdx.files.internal("risk_map_structure.json"));
    }

    public void init() {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map

        mapTexture = new Texture("risk_game_map.png");
        prepareMapPixmap(mapTexture);
        mapTexture.dispose();
        mapTexture = createColorLookupTexture();
        mapSprite = new Sprite(mapTexture);
//        mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);
        initColorLookupArray();

        setUpInputProcessor();
    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);
        ShaderProgram.pedantic = false;
    }

    private void prepareMapPixmap(Texture mapTexture) {
        if (mapPixmap != null)
            return;

        TextureData textureData = mapTexture.getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();

        mapPixmap = textureData.consumePixmap();
    }

    /**
     * Has a max limit of 255 different territories, because {@link #generateColor(byte)} works bytewise.
     */
    private Texture createColorLookupTexture() {
        // Makes `Pixmap.drawPixel()` directly set the pixel, instead of drawing the new color on top of the old one
        mapPixmap.setBlending(Pixmap.Blending.None);

        for (int y = 0; y < mapPixmap.getHeight(); y++) {
            for (int x = 0; x < mapPixmap.getWidth(); x++) {
                int pixel = mapPixmap.getPixel(x, y);
                int pixelAlpha = pixel & 0x000000FF;
                // (Unsigned) bit shift one byte to the right to discard the alpha value
                int pixelColor = pixel >>> 8;

                if (pixelAlpha > 0 && pixelAlpha < 0x80)
                    // If almost transparent, set to opaque
                    pixelAlpha = 0xFF;
                else if (pixelAlpha >= 0x80 && pixelAlpha < 0xFF)
                    // Else - if almost opaque, set to transparent
                    pixelAlpha = 0;

                Territory territory = territoryMap.getTerritory(pixelColor);
                if (territory != null)
                    pixelColor = generateColor(territory.colorIndex);
                int newPixel = (pixelColor << 8) | pixelAlpha;
                mapPixmap.drawPixel(x, y, newPixel);
            }
        }
        updateColorTerritoryMap();

        return new Texture(mapPixmap);
    }

    private void updateColorTerritoryMap() {
        Map<Integer, String> color_IDmap = territoryMap.getColor_IDmap();
        for (int color : new ArrayList<>(color_IDmap.keySet())) {
            String ID = color_IDmap.remove(color);
            color_IDmap.put(generateColor(territoryMap.getTerritory(ID).colorIndex), ID);
        }
        territoryMap.setColor_IDmap(color_IDmap);
    }

    private static int generateColor(byte index) {
        return (index << 2 * 8) | (index << 8) | (index);
    }

    // TODO: currently assigns each territory a random player color
    private void initColorLookupArray() {
        int[] playerColors = new int[] {0xFF0000, 0x0000FF};
        Random rand = new Random();
        for (Territory territory : territoryMap.getIDmap().values()) {
            int randomPlayer = rand.nextInt(playerColors.length);
            PLAYER_COLOR_LOOKUP.setColor(territory.colorIndex, playerColors[randomPlayer] << 8);
        }
    }

    // Move into appropriate place once needed
    private void setUpInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button != Input.Buttons.LEFT) // Only useful for desktop
                    return false;

                Vector3 _touchWorldPos = camera.unproject(new Vector3(screenX, screenY, 0));
                Vector2 touchWorldPos = new Vector2(_touchWorldPos.x, _touchWorldPos.y);
                if (mapSprite.getBoundingRectangle().contains(touchWorldPos)) {
                    Vector2 mapPos = worldPosToMapTexturePos(touchWorldPos);
                    Territory territory = getTerritory(mapPos);
                    System.out.println((territory != null) ? territory.name : "None");
                }

                return true;
            }
        });
    }

    private Vector2 worldPosToMapTexturePos(Vector2 worldPos) {
        Vector2 mapPos = new Vector2(worldPos).sub(mapSprite.getX(), mapSprite.getY());
        // Invert y coord, because the texture's origin is in the upper left corner
        mapPos.y = mapSprite.getHeight() - mapPos.y;
        // Round the coords, because it's needed for getting texture pixels
        mapPos.x = MathUtils.roundPositive(mapPos.x);
        mapPos.y = MathUtils.roundPositive(mapPos.y);
        return mapPos;
    }

    private Territory getTerritory(Vector2 mapPos) {
        // (Unsigned) bit shift one byte to the right to discard the alpha value
        int color = mapPixmap.getPixel((int)mapPos.x, (int)mapPos.y) >>> 8;
        return territoryMap.getTerritory(color);
    }

    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        float[] playerColorLookup = PLAYER_COLOR_LOOKUP.getFloatArray();
        mapShader.setUniform3fv("playerColorLookup", playerColorLookup, 0, playerColorLookup.length);
        mapSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        if (mapTexture.getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
        batch.dispose();
        mapShader.dispose();
    }
}
