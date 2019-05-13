package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public interface TroopObserver {
    void create(OrthographicCamera camera, TerritoryMap territoryMap, Texture circleTexture, Texture circleSelectTexture);

    void onMapRenderingChanged();

    void onSelectTerritory(Territory territory);

    void onTerritoryChangeNumTroops(Territory territory);
}
