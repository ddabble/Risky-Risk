package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.observer.BoardObserver;
import no.ntnu.idi.tdt4240.presenter.BoardPresenter;
import no.ntnu.idi.tdt4240.util.Utils;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;

public class BoardView extends ApplicationAdapter implements BoardObserver {
    private static final float CAMERA_MIN_ZOOM = 0.1f;

    private final OrthographicCamera camera;

    private SpriteBatch batch;
    private Sprite mapSprite;

    private ShaderProgram mapShader;

    private final ColorArray PLAYER_COLOR_LOOKUP = new ColorArray(0xFF + 1, 3);

    public BoardView(OrthographicCamera camera) {
        BoardPresenter.addObserver(this);
        this.camera = camera;
    }

    public Sprite getMapSprite() {
        return mapSprite;
    }

    /**
     * Must be called after {@link no.ntnu.idi.tdt4240.model.BoardModel} has been initialized.
     */
    @Override
    public void create(Texture mapTexture, List<Territory> territories, Map<Integer, Color> playerID_colorMap) {
        initShader();
        batch = new SpriteBatch(1, mapShader); // this sprite batch will only be used for 1 sprite: the map

        mapSprite = new Sprite(mapTexture);

        float mapAspectRatio = (float)mapTexture.getHeight() / mapTexture.getWidth();
        float screenAspectRatio = (float)Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        if (mapAspectRatio <= screenAspectRatio)
            mapSprite.setSize(camera.viewportWidth * screenAspectRatio / mapAspectRatio, camera.viewportHeight);
        else
            mapSprite.setSize(camera.viewportWidth, camera.viewportHeight * screenAspectRatio / mapAspectRatio);

        initColorLookupArray(territories, playerID_colorMap);
    }

    public void setInputProcessors(InputMultiplexer multiplexer) {
        multiplexer.addProcessor(new GestureDetector(new GestureDetector.GestureAdapter() {
            Vector2 lastPinchPointer1;
            Vector2 lastPinchPointer2;
            float initialPinchZoom;
            float initialPinchPointerWorldDistance;
//            float lastPinchPointerDistance;

            @Override
            public boolean tap(float screenX, float screenY, int count, int button) {
                if (button != Input.Buttons.LEFT) // Only for desktop
                    return false;

                Vector2 touchWorldPos = screenToWorldPos(screenX, screenY);
                if (!mapSprite.getBoundingRectangle().contains(touchWorldPos))
                    return false;

                Vector2 mapPos = worldPosToMapTexturePos(touchWorldPos);
                BoardPresenter.INSTANCE.onBoardClicked(mapPos);
                return true;
            }

            @Override
            public boolean pan(float screenX, float screenY, float deltaX, float deltaY) {
                Vector2 touchWorldDelta = screenToWorldPos(deltaX, deltaY).sub(screenToWorldPos(0, 0));
                camera.translate(-touchWorldDelta.x, -touchWorldDelta.y);
                ensureCameraIsWithinMap();
                BoardPresenter.INSTANCE.onMapMoved();
                return true;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                System.out.println("Fling: " + velocityX + ",\t" + velocityY);
                return false;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 currentPointer1, Vector2 currentPointer2) {
                if (lastPinchPointer1 == null) {
                    lastPinchPointer1 = initialPointer1;
                    lastPinchPointer2 = initialPointer2;
                    initialPinchZoom = camera.zoom;
                    initialPinchPointerWorldDistance = screenToWorldPos(initialPointer1).dst(screenToWorldPos(initialPointer2));
//                    lastPinchPointerDistance = initialPointer1.dst(initialPointer2);
                }


                float currentPinchPointerWorldDistance = screenToWorldPos(currentPointer1).dst(screenToWorldPos(currentPointer2));
//                System.out.println();
//                System.out.println(currentPinchPointerWorldDistance + "\t" + currentPointer1 + "\t" + currentPointer2);

                // initialZoom * initialDistance = newZoom * currentDistance
                //                       newZoom = initialZoom * initialDistance / currentDistance
                float newZoom = initialPinchZoom * initialPinchPointerWorldDistance / currentPinchPointerWorldDistance;
                newZoom = MathUtils.clamp(newZoom, CAMERA_MIN_ZOOM, 1f);

//                float currentPinchPointerDistance = currentPointer1.dst(currentPointer2);
//                if (Math.abs(newZoom - camera.zoom) / newZoom > 0.01f && Math.abs(currentPinchPointerDistance - lastPinchPointerDistance) < 10f) {
//                } else
                camera.zoom = newZoom;

//                System.out.println(camera.zoom);

//                Vector2 currentMidpoint = Utils.avg(currentPointer1, currentPointer2);
                Vector2 currentMidpoint = Utils.avg(currentPointer1, currentPointer2);
                Vector2 lastMidpoint = Utils.avg(lastPinchPointer1, lastPinchPointer2);
                Vector2 touchWorldDelta = screenToWorldPos(currentMidpoint).sub(screenToWorldPos(lastMidpoint));
                System.out.println(touchWorldDelta);
                camera.translate(-touchWorldDelta.x, -touchWorldDelta.y);
                ensureCameraIsWithinMap();
                BoardPresenter.INSTANCE.onMapMoved();

//                lastMidpoint = currentMidpoint;
//                lastPinchPointerDistance = currentPinchPointerDistance;
                lastPinchPointer1 = currentPointer1.cpy();
                lastPinchPointer2 = currentPointer2.cpy();
                return true;
            }

            @Override
            public void pinchStop() {
                lastPinchPointer1 = null;
                lastPinchPointer2 = null;
            }
        }));

        // Only for desktop
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                camera.zoom += amount / 10f;
                camera.zoom = MathUtils.clamp(camera.zoom, CAMERA_MIN_ZOOM, 1f);
                camera.update();
                BoardPresenter.INSTANCE.onMapMoved();
                return true;
            }
        });
    }

    private Vector2 screenToWorldPos(Vector2 screenPos) {
        return screenToWorldPos(screenPos.x, screenPos.y);
    }

    private Vector2 screenToWorldPos(float screenX, float screenY) {
        Vector3 touchWorldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(touchWorldPos.x, touchWorldPos.y);
    }

    private void ensureCameraIsWithinMap() {
        camera.update();

        Vector3 frustumCorner_distToOrigin = new Vector3().sub(camera.frustum.planePoints[0]);
        Vector3 frustumCorner_distFromMapEdge = new Vector3(mapSprite.getWidth(), mapSprite.getHeight(), 0)
                .sub(camera.frustum.planePoints[2]);

        if (frustumCorner_distToOrigin.x > 0)
            camera.translate(frustumCorner_distToOrigin.x, 0);
        if (frustumCorner_distToOrigin.y > 0)
            camera.translate(0, frustumCorner_distToOrigin.y);

        if (frustumCorner_distFromMapEdge.x < 0)
            camera.translate(frustumCorner_distFromMapEdge.x, 0);
        if (frustumCorner_distFromMapEdge.y < 0)
            camera.translate(0, frustumCorner_distFromMapEdge.y);

        camera.update();
    }

    private Vector2 worldPosToMapTexturePos(Vector2 worldPos) {
        Vector2 mapPos = worldPos.cpy().sub(mapSprite.getX(), mapSprite.getY());
        // Invert y coord, because the texture's origin is in the upper left corner
        mapPos.y = mapSprite.getHeight() - mapPos.y;

        Texture mapTexture = mapSprite.getTexture();
        Vector2 texturePos = new Vector2(mapPos.x / mapSprite.getWidth() * mapTexture.getWidth(),
                                         mapPos.y / mapSprite.getHeight() * mapTexture.getHeight());

        // Round the coords, because it's needed for getting texture pixels
        texturePos.x = MathUtils.roundPositive(texturePos.x);
        texturePos.y = MathUtils.roundPositive(texturePos.y);
        return texturePos;
    }

    public Vector2 troopCirclePosToWorldPos(Vector2 troopCirclePos) {
        Texture mapTexture = mapSprite.getTexture();
        return new Vector2(troopCirclePos.x / mapTexture.getWidth() * mapSprite.getWidth(),
                           troopCirclePos.y / mapTexture.getHeight() * mapSprite.getHeight());
    }

    public Vector2 screenPosRelativeToMap(Vector2 troopCirclePos) {
        Vector3 mapScreenPos = camera.project(new Vector3(mapSprite.getX(), mapSprite.getY(), 0));
        return new Vector2(mapScreenPos.x, mapScreenPos.y).add(troopCirclePos.x / camera.zoom, troopCirclePos.y / camera.zoom);
    }

    private void initShader() {
        Map<Integer, String> parsedShaders = GLSLshaders.parseShadersInFile("shaders/map.glsl");
        String vertexShader = parsedShaders.get(GL20.GL_VERTEX_SHADER);
        String fragmentShader = parsedShaders.get(GL20.GL_FRAGMENT_SHADER);
        mapShader = new ShaderProgram(vertexShader, fragmentShader);
        if (!mapShader.isCompiled())
            System.err.println("Error compiling mapShader:\n" + mapShader.getLog());
        ShaderProgram.pedantic = false;
    }

    private void initColorLookupArray(List<Territory> territories, Map<Integer, Color> playerID_colorMap) {
        for (Territory territory : territories) {
            Color playerColor = playerID_colorMap.get(territory.getOwnerID());
            PLAYER_COLOR_LOOKUP.setColor(territory.colorIndex, playerColor);
        }
    }

    @Override
    public void onTerritoryChangeColor(Territory territory, Color color) {
        PLAYER_COLOR_LOOKUP.setColor(territory.colorIndex, color);
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
