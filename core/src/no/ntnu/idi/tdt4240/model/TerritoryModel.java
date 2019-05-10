package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;

import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TerritoryModel {
    private static TerritoryModel INSTANCE;

    public final TerritoryMap TERRITORY_MAP;

    private TerritoryModel() {
        TERRITORY_MAP = TerritoryMap.parseJsonMapStructure(Gdx.files.internal("map/risk_map_structure.json"));
        TERRITORY_MAP.parseJsonBonusStrucutre(TERRITORY_MAP.getAllContinents(), Gdx.files.internal("map/risk_continent_bonuses.json"));
    }

    public static void init() {
        if (INSTANCE != null)
            return;
        INSTANCE = new TerritoryModel();
    }

    public static TerritoryModel getInstance() {
        return INSTANCE;
    }
}
