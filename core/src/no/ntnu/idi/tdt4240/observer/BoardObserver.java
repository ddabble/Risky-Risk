package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.data.Territory;

public interface BoardObserver {
    void create(OrthographicCamera camera, Texture mapTexture, List<Territory> territories, Map<Integer, Color> playerID_colorMap);

    void onTerritoryChangeColor(Territory territory, Color color);
}
