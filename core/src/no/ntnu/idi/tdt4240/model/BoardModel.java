package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class BoardModel {
    public static final BoardModel INSTANCE = new BoardModel();

    private TerritoryMap territoryMap;

    private Texture mapTexture;
    private Pixmap mapPixmap;

    private BoardModel() {}

    public Texture getMapTexture() {
        return mapTexture;
    }

    public void init() {
        territoryMap = TerritoryModel.getTerritoryMap();
        mapTexture = new Texture("map/risk_game_map.png");
        prepareMapPixmap(mapTexture);
        mapTexture.dispose();
        mapTexture = createColorLookupTexture();
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
     * Has a max limit of 255 different territories, because {@link #indexToColor(byte)} works bytewise.
     */
    private Texture createColorLookupTexture() {
        abstract class PixmapProcessor implements Runnable {
            final int processorIndex;

            PixmapProcessor(int processorIndex) {
                this.processorIndex = processorIndex;
            }
        }

        List<Thread> threads = new ArrayList<>();
        IntBuffer pixelBuffer = mapPixmap.getPixels().asIntBuffer();
        final int numPixels = pixelBuffer.remaining();
        final int numProcessors = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < numProcessors; i++) {
            Thread thread = new Thread(new PixmapProcessor(i) {
                @Override
                public void run() {
                    int startI = processorIndex * numPixels / numProcessors;
                    int endI = (processorIndex + 1) * numPixels / numProcessors;
                    processPixelsInRange(startI, endI, pixelBuffer, territoryMap);
                }
            });
            thread.start();
            threads.add(thread);
        }

        // Wait for threads to finish processing
        try {
            for (Thread thread : threads)
                thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        updateColorTerritoryMap();
        return new Texture(mapPixmap);
    }

    private static void processPixelsInRange(int startI, int endI, IntBuffer buf, TerritoryMap territoryMap) {
        for (int i = startI; i < endI; i++) {
                int pixel = buf.get(i);
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
                    pixelColor = indexToColor(territory.colorIndex);
                int newPixel = (pixelColor << 8) | pixelAlpha;
                buf.put(i, newPixel);
        }
    }

    private static int indexToColor(byte index) {
        return (index << 2 * 8) | (index << 8) | (index);
    }

    private void updateColorTerritoryMap() {
        Map<Integer, String> color_IDmap = territoryMap.getColor_IDmap();
        for (int color : new ArrayList<>(color_IDmap.keySet())) {
            String ID = color_IDmap.remove(color);
            color_IDmap.put(indexToColor(territoryMap.getTerritory(ID).colorIndex), ID);
        }
        territoryMap.setColor_IDmap(color_IDmap);
    }

    public Territory getTerritory(Vector2 mapPos) {
        // (Unsigned) bit shift one byte to the right to discard the alpha value
        int color = mapPixmap.getPixel((int)mapPos.x, (int)mapPos.y) >>> 8;
        return territoryMap.getTerritory(color);
    }

    public void reset() {
        if (mapTexture.getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
    }
}
