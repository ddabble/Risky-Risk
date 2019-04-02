package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
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

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import no.ntnu.idi.tdt4240.Components.BattleModel;
import no.ntnu.idi.tdt4240.Components.Territory;
import no.ntnu.idi.tdt4240.util.GLSLshaders;


public class BoardSystem extends ApplicationAdapterEntitySystem {
    private final Map<Integer, Territory> COLOR_TERRITORY_MAP = new HashMap<Integer, Territory>() {{
        // North America
        put(0x6A8600, new Territory("North America 1"));
        put(0x3F5437, new Territory("North America 2"));
        put(0xFFFF00, new Territory("North America 3"));
        put(0x949449, new Territory("North America 4"));
        put(0xD1FF80, new Territory("North America 5"));
        put(0x505027, new Territory("North America 6"));
        put(0x808000, new Territory("North America 7"));
        put(0xFFFF80, new Territory("North America 8"));
        put(0xA27300, new Territory("North America 9"));

        // South America
        put(0xFF8080, new Territory("South America 1"));
        put(0x800000, new Territory("South America 2"));
        put(0x804040, new Territory("South America 3"));
        put(0xFF0000, new Territory("South America 4"));

        // Europe
        put(0x0000FF, new Territory("Europe 1"));
        put(0x0080FF, new Territory("Europe 2"));
        put(0x004080, new Territory("Europe 3"));
        put(0x000080, new Territory("Europe 4"));
        put(0x000041, new Territory("Europe 5"));
        put(0xF380FF, new Territory("Europe 6"));
        put(0x43378F, new Territory("Europe 7"));

        // Africa
        put(0xFF915B, new Territory("Africa 1"));
        put(0x972900, new Territory("Africa 2"));
        put(0xAE5700, new Territory("Africa 3"));
        put(0xFF8000, new Territory("Africa 4"));
        put(0x804000, new Territory("Africa 5"));
        put(0x6F4B00, new Territory("Africa 6"));

        // Asia
        put(0x562913, new Territory("Asia 1"));
        put(0x006765, new Territory("Asia 2"));
        put(0x00956D, new Territory("Asia 3"));
        put(0x008040, new Territory("Asia 4"));
        put(0x80A480, new Territory("Asia 5"));
        put(0xB3AA00, new Territory("Asia 6"));
        put(0x004000, new Territory("Asia 7"));
        put(0x627451, new Territory("Asia 8"));
        put(0x80FF00, new Territory("Asia 9"));
        put(0x008000, new Territory("Asia 10"));
        put(0x008080, new Territory("Asia 11"));
        put(0x80FF80, new Territory("Asia 12"));

        // Australia
        put(0x8000FF, new Territory("Australia 1"));
        put(0xFF00FF, new Territory("Australia 2"));
        put(0x800040, new Territory("Australia 3"));
        put(0x400040, new Territory("Australia 4"));
    }};

    private ImmutableArray<Entity> entities;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture mapTexture;
    private Pixmap mapPixmap;
    private Sprite mapSprite;

    private ShaderProgram mapShader;

    public BoardSystem(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    public void addedToEngine(Engine engine) {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map

        mapTexture = new Texture("risk_game_map.png");
        mapSprite = new Sprite(mapTexture);
//        mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);
        prepareMapPixmap();

        setUpInputProcessor();
    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);
    }

    private void prepareMapPixmap() {
        if (mapPixmap != null)
            return;

        TextureData textureData = mapSprite.getTexture().getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();

        mapPixmap = textureData.consumePixmap();
    }

    // Move into appropriate place once needed
    private void setUpInputProcessor() {
        final BattleModel battMod = new BattleModel();
        final Random random = new Random();

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
        return COLOR_TERRITORY_MAP.get(color);
    }

    private String getTileID(Vector2 mapPos) {
        int color = mapPixmap.getPixel((int)mapPos.x, (int)mapPos.y) >>> 8;
        String hex = Integer.toHexString(color);
        return hex;
    }

    public void render(OrthographicCamera camera) {
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        mapSprite.draw(batch);
        batch.end();
    }

    public void Writer(String lineToWrite) {
        try {
            FileWriter writer = new FileWriter("Coords.txt", true);
            writer.write(lineToWrite);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {
        if (mapSprite.getTexture().getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
        batch.dispose();
        mapShader.dispose();
    }
}
