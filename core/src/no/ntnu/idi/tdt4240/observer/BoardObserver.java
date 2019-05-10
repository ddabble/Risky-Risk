package no.ntnu.idi.tdt4240.observer;

import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;

public interface BoardObserver {
    void create(Texture mapTexture, List<Territory> territories, Map<Integer, Integer> playerID_colorMap);
}
