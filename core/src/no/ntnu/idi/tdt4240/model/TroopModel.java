package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Texture;

import no.ntnu.idi.tdt4240.model.data.Territory;

public class TroopModel {
    public static final TroopModel INSTANCE = new TroopModel();

    private Texture circleTexture;
    private Texture circleSelectTexture;

    private Territory selectedTerritory;

    private TroopModel() {}

    public Texture getCircleTexture() {
        return circleTexture;
    }

    public Texture getCircleSelectTexture() {
        return circleSelectTexture;
    }

    public void onSelectTerritory(Territory territory) {
        selectedTerritory = territory;
    }

    public static void init() {
        INSTANCE._init();
    }

    private void _init() {
        circleTexture = new Texture("map/troop_circle.png");
        circleSelectTexture = new Texture("map/troop_circle_select.png");
    }

    public static void reset() {
        INSTANCE._reset();
    }

    private void _reset() {
        circleSelectTexture.dispose();
        circleTexture.dispose();
    }
}
