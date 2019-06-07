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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.observer.BoardObserver;
import no.ntnu.idi.tdt4240.presenter.BoardPresenter;
import no.ntnu.idi.tdt4240.presenter.PhasePresenter;
import no.ntnu.idi.tdt4240.util.Utils;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;
import no.ntnu.idi.tdt4240.util.gl.GLSLshaders;

public class BoardView extends ApplicationAdapter implements BoardObserver {
    enum OffsetDirection {
        HORIZONTAL, VERTICAL
    }

    private static final float CAMERA_MIN_ZOOM = 0.1f;
    private static final float CAMERA_FRICTION_COEFFICIENT = GameView.getWorldWidth() * 0.5f;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sprite mapSprite;

    private ShaderProgram mapShader;

    private final ColorArray PLAYER_COLOR_LOOKUP = new ColorArray(0xFF + 1, 3);

    private Vector2 flingVelocity;
    private long flingStartTime;
    private float flingDuration;

    public BoardView() {
        BoardPresenter.addObserver(this);
    }

    public Vector2 troopCirclePosToWorldPos(Vector2 troopCirclePos) {
        Texture mapTexture = mapSprite.getTexture();
        return new Vector2(troopCirclePos.x / mapTexture.getWidth() * mapSprite.getWidth(),
                           troopCirclePos.y / mapTexture.getHeight() * mapSprite.getHeight());
    }

    public Vector2 screenPosRelativeToMap(Vector2 troopCirclePos) {
        Vector3 mapScreenPos = camera.project(new Vector3(mapSprite.getX(), mapSprite.getY(), 0));

        Texture mapTexture = mapSprite.getTexture();
        Vector2 mapScreenSize = Utils.worldDistToScreenDist(mapSprite.getWidth(), mapSprite.getHeight(), camera);
        float textureScreenWidthRatio = mapScreenSize.x / mapTexture.getWidth();
        float textureScreenHeightRatio = mapScreenSize.y / mapTexture.getHeight();

        return new Vector2(mapScreenPos.x, mapScreenPos.y).add(troopCirclePos.x * textureScreenWidthRatio,
                                                               troopCirclePos.y * textureScreenHeightRatio);
    }

    /**
     * Must be called after {@link no.ntnu.idi.tdt4240.model.BoardModel} has been initialized.
     */
    @Override
    public void create(OrthographicCamera camera, Texture mapTexture, List<Territory> territories, Map<Integer, Color> playerID_colorMap) {
        this.camera = camera;
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
            float initialPinchPointerDistance;

            long lastPinchStop = TimeUtils.millis();

            @Override
            public boolean touchDown(float touchX, float touchY, int pointer, int button) {
                if (button != Input.Buttons.LEFT) // Only for desktop
                    return false;
                stopFling();

                return true;
            }

            @Override
            public boolean tap(float touchX, float touchY, int count, int button) {
                if (button != Input.Buttons.LEFT) // Only for desktop
                    return false;

                Vector2 touchWorldPos = Utils.touchToWorldPos(touchX, touchY, camera);
                if (!mapSprite.getBoundingRectangle().contains(touchWorldPos))
                    return false;

                Vector2 mapPos = worldPosToMapTexturePos(touchWorldPos);
                BoardPresenter.INSTANCE.onBoardClicked(mapPos);
                return true;
            }

            @Override
            public boolean pan(float touchX, float touchY, float deltaX, float deltaY) {
                stopFling();

                Vector2 touchWorldDelta = Utils.touchDistToWorldDist(deltaX, deltaY, camera);
                camera.translate(-touchWorldDelta.x, -touchWorldDelta.y);
                ensureCameraIsWithinMap();

                PhasePresenter.INSTANCE.onMapRenderingChanged();
                return true;
            }

            @Override
            public boolean fling(float velocityX, float velocityY, int button) {
                if (button != Input.Buttons.LEFT) // Only for desktop
                    return false;
                // Eliminates jerky flings right after stopping pinching
                if (TimeUtils.timeSinceMillis(lastPinchStop) < 100)
                    return false;

                flingVelocity = Utils.touchDistToWorldDist(velocityX, velocityY, camera);
                flingStartTime = TimeUtils.millis();
                flingDuration = flingVelocity.len() / CAMERA_FRICTION_COEFFICIENT;
                return true;
            }

            @Override
            public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 currentPointer1, Vector2 currentPointer2) {
                if (lastPinchPointer1 == null) {
                    lastPinchPointer1 = initialPointer1;
                    lastPinchPointer2 = initialPointer2;
                    initialPinchZoom = camera.zoom;
                    initialPinchPointerDistance = initialPointer1.dst(initialPointer2);

                    stopFling();
                }

                handleZooming(currentPointer1, currentPointer2);
                handlePanning(currentPointer1, currentPointer2);
                ensureCameraIsWithinMap();

                PhasePresenter.INSTANCE.onMapRenderingChanged();

                lastPinchPointer1 = currentPointer1.cpy();
                lastPinchPointer2 = currentPointer2.cpy();
                return true;
            }

            private void handleZooming(Vector2 currentPointer1, Vector2 currentPointer2) {
                float currentPinchPointerDistance = currentPointer1.dst(currentPointer2);

                // initialZoom * initialDistance = newZoom * currentDistance
                //                       newZoom = initialZoom * initialDistance / currentDistance
                camera.zoom = initialPinchZoom * initialPinchPointerDistance / currentPinchPointerDistance;
                camera.zoom = MathUtils.clamp(camera.zoom, CAMERA_MIN_ZOOM, 1f);

                handleOffCenteredZooming(Utils.avg(currentPointer1, currentPointer2));
            }

            private void handlePanning(Vector2 currentPointer1, Vector2 currentPointer2) {
                Vector2 currentMidpoint = Utils.avg(currentPointer1, currentPointer2);
                Vector2 lastMidpoint = Utils.avg(lastPinchPointer1, lastPinchPointer2);
                Vector2 touchWorldDelta = Utils.touchToWorldPos(currentMidpoint, camera).sub(Utils.touchToWorldPos(lastMidpoint, camera));
                camera.translate(-touchWorldDelta.x, -touchWorldDelta.y);
            }

            @Override
            public void pinchStop() {
                lastPinchPointer1 = null;
                lastPinchPointer2 = null;
                lastPinchStop = TimeUtils.millis();
            }
        }));

        // Only for desktop
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(int amount) {
                stopFling();

                float newZoom = camera.zoom + amount / 10f;
                newZoom = MathUtils.clamp(newZoom, CAMERA_MIN_ZOOM, 1f);
                if (newZoom == camera.zoom)
                    return true;

                camera.zoom = newZoom;
                handleOffCenteredZooming(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

                ensureCameraIsWithinMap();

                PhasePresenter.INSTANCE.onMapRenderingChanged();
                return true;
            }
        });
    }

    private void stopFling() {
        if (flingVelocity != null)
            flingVelocity = null;
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

    private OffsetDirection ensureCameraIsWithinMap() {
        camera.update();

        Vector3 frustumCorner_distToOrigin = new Vector3().sub(camera.frustum.planePoints[0]);
        Vector3 frustumCorner_distFromMapEdge = new Vector3(mapSprite.getWidth(), mapSprite.getHeight(), 0)
                .sub(camera.frustum.planePoints[2]);

        OffsetDirection offsetDirection = null;

        if (frustumCorner_distToOrigin.x > 0) {
            camera.translate(frustumCorner_distToOrigin.x, 0);
            offsetDirection = OffsetDirection.HORIZONTAL;
        } else if (frustumCorner_distFromMapEdge.x < 0) {
            camera.translate(frustumCorner_distFromMapEdge.x, 0);
            offsetDirection = OffsetDirection.HORIZONTAL;
        }

        if (frustumCorner_distToOrigin.y > 0) {
            camera.translate(0, frustumCorner_distToOrigin.y);
            offsetDirection = OffsetDirection.VERTICAL;
        } else if (frustumCorner_distFromMapEdge.y < 0) {
            camera.translate(0, frustumCorner_distFromMapEdge.y);
            offsetDirection = OffsetDirection.VERTICAL;
        }

        if (offsetDirection != null)
            camera.update();

        return offsetDirection;
    }

    private void handleOffCenteredZooming(Vector2 zoomPoint) {
        // Note: camera always zooms in/out from the camera's center
        Vector3 prevBottomLeftFrustumCorner = Utils.getBottomLeftFrustumCorner(camera);
        Vector2 zoomPoint_world = Utils.touchToWorldPos(zoomPoint, camera);
        camera.update();
        Vector3 frustumEdgeDelta = Utils.getBottomLeftFrustumCorner(camera).sub(prevBottomLeftFrustumCorner);

        Vector3 cameraPos = camera.position;
        // Has a value in the range [-1, 1];
        // -1 is on the left frustum edge, 0 is in the camera center, and +1 is on the right frustum edge
        Vector2 zoomPointOffCenterPercentage = new Vector2((zoomPoint_world.x - prevBottomLeftFrustumCorner.x)
                                                           / (cameraPos.x - prevBottomLeftFrustumCorner.x) - 1,
                                                           (zoomPoint_world.y - prevBottomLeftFrustumCorner.y)
                                                           / (cameraPos.y - prevBottomLeftFrustumCorner.y) - 1);

        camera.translate(zoomPointOffCenterPercentage.x * frustumEdgeDelta.x,
                         zoomPointOffCenterPercentage.y * frustumEdgeDelta.y);
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
        if (flingVelocity != null)
            handleCameraFling();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float[] playerColorLookup = PLAYER_COLOR_LOOKUP.getFloatArray();
        mapShader.setUniform3fv("playerColorLookup", playerColorLookup, 0, playerColorLookup.length);
        mapSprite.draw(batch);
        batch.end();
    }

    private void handleCameraFling() {
        final float deltaTime = Gdx.graphics.getDeltaTime();
        camera.translate(new Vector2(-flingVelocity.x * deltaTime,
                                     -flingVelocity.y * deltaTime));

        OffsetDirection offsetDirection = ensureCameraIsWithinMap();
        PhasePresenter.INSTANCE.onMapRenderingChanged();
        if (offsetDirection != null) {
            switch (offsetDirection) {
                case HORIZONTAL:
                    flingVelocity.x = 0;
                    break;

                case VERTICAL:
                    flingVelocity.y = 0;
                    break;
            }
            if (flingVelocity.isZero()) {
                flingVelocity = null;
                return;
            }
        }

        float timeSinceFlingStart = TimeUtils.timeSinceMillis(flingStartTime) / 1000f;
        float flingDurationPercentage = timeSinceFlingStart / flingDuration;
        flingVelocity.interpolate(Vector2.Zero, flingDurationPercentage, Interpolation.smooth);

        if (flingDurationPercentage >= 1f)
            flingVelocity = null;
    }

    @Override
    public void dispose() {
        batch.dispose();
        mapShader.dispose();
    }
}
