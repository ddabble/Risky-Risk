package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
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
