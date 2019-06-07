package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.io.File;

import no.ntnu.idi.tdt4240.view.GameView;

public class Utils {
    public static Vector2 avg(Vector2 v1, Vector2 v2) {
        return new Vector2((v1.x + v2.x) / 2,
                           (v1.y + v2.y) / 2);
    }

    public static Vector2 touchToScreenPos(Vector2 touchPos) {
        return new Vector2(touchPos.x, Gdx.graphics.getHeight() - touchPos.y);
    }

    public static Vector2 screenToTouchPos(Vector2 screenPos) {
        return new Vector2(screenPos.x, Gdx.graphics.getHeight() - screenPos.y);
    }

    public static Vector2 screenToWorldPos(Vector2 screenPos, Camera camera) {
        return touchToWorldPos(screenToTouchPos(screenPos), camera);
    }

    public static Vector2 touchToWorldPos(Vector2 touchPos, Camera camera) {
        return touchToWorldPos(touchPos.x, touchPos.y, camera);
    }

    public static Vector2 touchToWorldPos(float touchX, float touchY, Camera camera) {
        Vector3 worldPos = camera.unproject(new Vector3(touchX, touchY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }

    public static Vector2 touchDistToWorldDist(float xDist, float yDist, Camera camera) {
        return touchToWorldPos(xDist, yDist, camera).sub(touchToWorldPos(0, 0, camera));
    }

    public static Vector2 worldToScreenPos(Vector2 worldPos, Camera camera) {
        return worldToScreenPos(worldPos.x, worldPos.y, camera);
    }

    public static Vector2 worldToScreenPos(float worldX, float worldY, Camera camera) {
        Vector3 screenPos = camera.project(new Vector3(worldX, worldY, 0));
        return new Vector2(screenPos.x, screenPos.y);
    }

    public static Vector2 worldDistToScreenDist(float xDist, float yDist, Camera camera) {
        return worldToScreenPos(xDist, yDist, camera).sub(worldToScreenPos(0, 0, camera));
    }

    public static Vector3 getBottomLeftFrustumCorner(Camera camera) {
        return camera.frustum.planePoints[0].cpy();
    }

    public static Vector3 getTopRightFrustumCorner(Camera camera) {
        return camera.frustum.planePoints[2].cpy();
    }

    public static void setSizeOfSprite(Sprite sprite, float spriteSize_mapRatio) {
        final float spriteSize = GameView.getWorldWidth() * spriteSize_mapRatio;
        sprite.setSize(spriteSize, spriteSize);
        sprite.setOriginCenter();
    }

    public static String getLinkToLineInFile(File file, int lineNumber) {
        return file.getPath() + "(" + file.getName() + ":" + lineNumber + ")";
    }

    public static String getLinkToCharInFile(File file, String fileContents, int charIndex) {
        int lineNumber = 1 + countOccurrences(fileContents.substring(0, charIndex), "\n");
        return getLinkToLineInFile(file, lineNumber);
    }

    public static int countOccurrences(String src, String findString) {
        int count = 0;
        int findStringLength = findString.length();

        for (int lastIndex = src.indexOf(findString); lastIndex != -1; ) {
            count++;
            lastIndex = src.indexOf(findString, lastIndex + findStringLength);
        }

        return count;
    }
}
