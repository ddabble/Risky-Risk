package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.Gdx;

import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class TerritoryModel {
    public static final TerritoryModel INSTANCE = new TerritoryModel();

    private TerritoryMap TERRITORY_MAP;

    private TerritoryModel() {}

    public static TerritoryMap getTerritoryMap() {
        return INSTANCE.TERRITORY_MAP;
    }

    public static void init() {
        if (INSTANCE.TERRITORY_MAP != null)
            return;

        INSTANCE.TERRITORY_MAP = TerritoryMap.parseJsonMapStructure(Gdx.files.internal("map/risk_map_structure.json"));
        INSTANCE.TERRITORY_MAP.parseJsonBonusStrucutre(INSTANCE.TERRITORY_MAP.getAllContinents(), Gdx.files.internal("map/risk_continent_bonuses.json"));
    }
}
