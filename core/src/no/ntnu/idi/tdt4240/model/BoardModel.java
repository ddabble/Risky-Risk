package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import no.ntnu.idi.tdt4240.util.TerritoryMap;
import no.ntnu.idi.tdt4240.util.gl.ColorArray;

public class BoardModel {
    private final TerritoryMap TERRITORY_MAP;
    private final ColorArray PLAYER_COLOR_LOOKUP = new ColorArray(0xFF + 1, 3);

    private Texture mapTexture;
    private Pixmap mapPixmap;

    public BoardModel() {
        TERRITORY_MAP = TerritoryMap.parseJsonMapStructure(Gdx.files.internal("risk_map_structure.json"));
    }

    public ColorArray getPlayerColorLookup() {
        return PLAYER_COLOR_LOOKUP;
    }

    public Texture getMapTexture() {
        return mapTexture;
    }

    public void init() {
        mapTexture = new Texture("risk_game_map.png");
        prepareMapPixmap(mapTexture);
        mapTexture.dispose();
        mapTexture = createColorLookupTexture();

        initColorLookupArray();
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

                Territory territory = TERRITORY_MAP.getTerritory(pixelColor);
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
        Map<Integer, String> color_IDmap = TERRITORY_MAP.getColor_IDmap();
        for (int color : new ArrayList<>(color_IDmap.keySet())) {
            String ID = color_IDmap.remove(color);
            color_IDmap.put(generateColor(TERRITORY_MAP.getTerritory(ID).colorIndex), ID);
        }
        TERRITORY_MAP.setColor_IDmap(color_IDmap);
    }

    private static int generateColor(byte index) {
        return (index << 2 * 8) | (index << 8) | (index);
    }

    // TODO: currently assigns each territory a random player color
    private void initColorLookupArray() {
        int[] playerColors = new int[] {0xFF0000, 0x0000FF};
        Random rand = new Random();
        for (Territory territory : TERRITORY_MAP.getIDmap().values()) {
            int randomPlayer = rand.nextInt(playerColors.length);
            PLAYER_COLOR_LOOKUP.setColor(territory.colorIndex, playerColors[randomPlayer] << 8);
        }
    }

    public Vector2 worldPosToMapTexturePos(Vector2 worldPos, Sprite mapSprite) {
        Vector2 mapPos = new Vector2(worldPos).sub(mapSprite.getX(), mapSprite.getY());
        // Invert y coord, because the texture's origin is in the upper left corner
        mapPos.y = mapSprite.getHeight() - mapPos.y;
        // Round the coords, because it's needed for getting texture pixels
        mapPos.x = MathUtils.roundPositive(mapPos.x);
        mapPos.y = MathUtils.roundPositive(mapPos.y);
        return mapPos;
    }

    public Territory getTerritory(Vector2 mapPos) {
        // (Unsigned) bit shift one byte to the right to discard the alpha value
        int color = mapPixmap.getPixel((int)mapPos.x, (int)mapPos.y) >>> 8;
        return TERRITORY_MAP.getTerritory(color);
    }

    public void reset() {
        if (mapTexture.getTextureData().disposePixmap())
            mapPixmap.dispose();
        mapTexture.dispose();
    }
}
