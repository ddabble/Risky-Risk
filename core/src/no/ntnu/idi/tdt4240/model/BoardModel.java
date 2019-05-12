package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class BoardModel {
    public static final BoardModel INSTANCE = new BoardModel();

    private TerritoryMap territoryMap;

    private Texture mapTexture;
    private Pixmap mapPixmap;

    private IGPGSClient client;

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

        //if we have an active match in the client it means we are playing an online game,
        //disregard the data loaded from TerritoryModel and instead fill in data from
        //the match object
        if(client.matchActive()) {
            if(client.getmRiskyTurn().isDataInitialized()) {
                client.getmRiskyTurn().getTerritoryMapData(territoryMap);
            } else  { //init data
                client.getmRiskyTurn().updateData(territoryMap, 0);
            }
        }
    }

    public void setGPGSClient(IGPGSClient client) {
        this.client = client;
    }

    private void prepareMapPixmap(Texture mapTexture) {
        if (mapPixmap != null)
            return;

        TextureData textureData = mapTexture.getTextureData();
        if (!textureData.isPrepared())
            textureData.prepare();

        mapPixmap = textureData.consumePixmap();
    }

    public int getNumberOfPlayers() {
        return client.getmRiskyTurn().getNumberOfPlayers();
    }

    //used to check if this board is held online
    //or only localy. This determines how it should be
    //passed to the next player
    public boolean isOnlineMatch() {
        return client.matchActive();
    }

    //this function is used to tell the board
    //that it should update its online match state
    //i.e the data in riskyTurn and send it to the server
    public void updateAndSendMatchData() {

        client.getmRiskyTurn().updateData(territoryMap, TurnModel.INSTANCE.getCurrentPlayerID());
        client.onDoneClicked();
    }

    /**
     * On desktop:
     * Has a max limit of 255 different territories, because {@link #indexToColor(byte)} works bytewise.
     * <p>
     * On Android:
     * Has a max limit of 224 different territories, because of the OpenGL ES 3.0 specification.
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

                // If transparent, skip pixel
                if (pixelAlpha == 0)
                    continue;

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
        Map<Integer, Territory> color_territoryMap = territoryMap.getColor_territoryMap();
        for (int color : new ArrayList<>(color_territoryMap.keySet())) {
            Territory territory = color_territoryMap.remove(color);
            color_territoryMap.put(indexToColor(territory.colorIndex), territory);
        }
        territoryMap.setColor_territoryMap(color_territoryMap);
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
