package no.ntnu.idi.tdt4240.view;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TroopView extends ApplicationAdapter {
    private final TerritoryMap territoryMap;

    private SpriteBatch batch;
    private Texture circleTexture;
    private Map<Territory, Sprite> circleSpriteMap;

    public TroopView(TerritoryMap territoryMap) {
        this.territoryMap = territoryMap;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();

        createCircleSprites();
    }

    private void createCircleSprites() {
        circleTexture = new Texture("map/troop_circle.png");

        circleSpriteMap = new HashMap<>();
        for (Territory territory : territoryMap.getIDmap().values()) {
            Vector2 circlePos = territory.getTroopCircleVector();
            Sprite sprite = new Sprite(circleTexture);
            sprite.setOriginBasedPosition(circlePos.x, circlePos.y);
            circleSpriteMap.put(territory, sprite);
        }
    }

    @Override
    public void render() {
        batch.begin();
        for (Sprite sprite : circleSpriteMap.values())
            sprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        circleTexture.dispose();
        batch.dispose();
    }
}
