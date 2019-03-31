package no.ntnu.idi.tdt4240.EntitySystems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.idi.tdt4240.util.GLSLshaders;

import static com.badlogic.gdx.graphics.GL20.*;


public class BoardSystem extends ApplicationAdapterEntitySystem {
    private static final Map<Integer, String> MAP_COLORS_TILES = new HashMap<Integer, String>() {{
        // North America
        put(0x6A8600, "North America 1");
        put(0x3F5437, "North America 2");
        put(0xFFFF00, "North America 3");
        put(0x949449, "North America 4");
        put(0xD1FF80, "North America 5");
        put(0x505027, "North America 6");
        put(0x808000, "North America 7");
        put(0xFFFF80, "North America 8");
        put(0xA27300, "North America 9");

        // South America
        put(0xFF8080, "South America 1");
        put(0x800000, "South America 2");
        put(0x804040, "South America 3");
        put(0xFF0000, "South America 4");

        // Europe
        put(0x0000FF, "Europe 1");
        put(0x0080FF, "Europe 2");
        put(0x004080, "Europe 3");
        put(0x000080, "Europe 4");
        put(0x000041, "Europe 5");
        put(0xF380FF, "Europe 6");
        put(0x43378F, "Europe 7");

        // Africa
        put(0xFF915B, "Africa 1");
        put(0x972900, "Africa 2");
        put(0xAE5700, "Africa 3");
        put(0xFF8000, "Africa 4");
        put(0x804000, "Africa 5");
        put(0x6F4B00, "Africa 6");

        // Asia
        put(0x562913, "Asia 1");
        put(0x006765, "Asia 2");
        put(0x00956D, "Asia 3");
        put(0x008040, "Asia 4");
        put(0x80A480, "Asia 5");
        put(0xB3AA00, "Asia 6");
        put(0x004000, "Asia 7");
        put(0x627451, "Asia 8");
        put(0x80FF00, "Asia 9");
        put(0x008000, "Asia 10");
        put(0x008080, "Asia 11");
        put(0x80FF80, "Asia 12");

        // Australia
        put(0x8000FF, "Australia 1");
        put(0xFF00FF, "Australia 2");
        put(0x800040, "Australia 3");
        put(0x400040, "Australia 4");
    }};

    private ImmutableArray<Entity> entities;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture mapTexture;
    private Pixmap mapPixmap;
    private Sprite mapSprite;

    private ShaderProgram mapShader;
    private Mesh mapMesh;

    public BoardSystem(OrthographicCamera camera) {
        super();
        this.camera = camera;
    }

    public void addedToEngine(Engine engine) {
        batch = new SpriteBatch();

        mapTexture = new Texture("risk_game_map.png");
        mapSprite = new Sprite(mapTexture);
//        mapSprite.setSize(mapTexture.getWidth() / 2f, mapTexture.getHeight() / 2f);
        prepareMapPixmap();
        initShader();

        setUpInputProcessor();
    }

    private void prepareMapPixmap() {
        if (mapPixmap != null)
            return;

        TextureData textureData = mapSprite.getTexture().getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();

        mapPixmap = textureData.consumePixmap();
    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);

        mapMesh = new Mesh(true, 4, 6, VertexAttribute.Position(), VertexAttribute.ColorUnpacked(), VertexAttribute.TexCoords(0));
        // TODO: fix vertex coords
        mapMesh.setVertices(new float[] {
                // Position:   , Color:    , UV:
                -0.9f, -0.9f, 0, 1, 1, 1, 1, 0, 1,
                0.9f, -0.9f, 0, 1, 1, 1, 1, 1, 1,
                0.9f, 0.9f, 0, 1, 1, 1, 1, 1, 0,
                -0.9f, 0.9f, 0, 1, 1, 1, 1, 0, 0});
        mapMesh.setIndices(new short[] {0, 1, 2, 2, 3, 0});
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
                    System.out.println(getTile(mapPos));
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

    private String getTile(Vector2 mapPos) {
        // (Unsigned) bit shift one byte to the right to discard the alpha value
        int color = mapPixmap.getPixel((int)mapPos.x, (int)mapPos.y) >>> 8;
        return MAP_COLORS_TILES.get(color);
    }

    public void render(SpriteBatch batch) {
        mapSprite.getTexture().bind();
        mapShader.begin();
        // Enable alpha blending, because the map texture contains an alpha channel
        Gdx.gl.glEnable(GL_BLEND);
        Gdx.gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        mapShader.setUniformMatrix("u_projTrans", new Matrix4()); // TODO: provide the camera's projection matrix instead
        mapShader.setUniformi("u_texture", 0);
        mapMesh.render(mapShader, GL20.GL_TRIANGLES);
        mapShader.end();
    }

    @Override
    public void dispose() {
        mapShader.dispose();
        if (mapSprite.getTexture().getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
        batch.dispose();
    }
}
