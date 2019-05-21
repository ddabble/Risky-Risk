package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.model.data.Continent;
import no.ntnu.idi.tdt4240.model.data.Territory;

public class TerritoryMap {
    private Map<String, Territory> ID_territoryMap;
    private Map<Integer, Territory> color_territoryMap;

    // TODO: move to separate class
    private Map<String, Continent> continentMap;

    public TerritoryMap(Map<String, Territory> ID_territoryMap, Map<Integer, Territory> color_territoryMap) {
        this.ID_territoryMap = ID_territoryMap;
        this.color_territoryMap = color_territoryMap;
    }

    /**
     * @param ID a territory's lowercase ID
     */
    public Territory getTerritory(String ID) {
        return ID_territoryMap.get(ID);
    }

    public Territory getTerritory(int color) {
        return color_territoryMap.get(color);
    }

    public List<Territory> getAllTerritories() {
        return new ArrayList<>(ID_territoryMap.values());
    }

    public List<Continent> getAllContinents() {
        return new ArrayList<>(continentMap.values());
    }

    public Map<String, Territory> getID_territoryMap() {
        return new HashMap<>(ID_territoryMap);
    }

    public Map<Integer, Territory> getColor_territoryMap() {
        return new HashMap<>(color_territoryMap);
    }

    public void setColor_territoryMap(Map<Integer, Territory> color_territoryMap) {
        this.color_territoryMap = new HashMap<>(color_territoryMap);
    }

    public static TerritoryMap parseJsonMapStructure(FileHandle jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        Map readJson;
        try {
            readJson = mapper.readValue(jsonFile.readString(), Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        @SuppressWarnings("unchecked")
        Map<String, Map<String, Map<String, Object>>> continentStructureMap = (Map<String, Map<String, Map<String, Object>>>)readJson.get("continents");
        @SuppressWarnings("unchecked")
        Map<String, Integer> continentBonusMap = (Map<String, Integer>)readJson.get("bonus_troops");

        Map<String, List<Territory>> continentTerritoriesMap = new HashMap<>();
        TerritoryMap territoryMap = parseJsonMapStructure(continentStructureMap, continentTerritoriesMap);
        territoryMap.continentMap = getContinentMap(continentBonusMap, continentTerritoriesMap);
        return territoryMap;
    }

    private static TerritoryMap parseJsonMapStructure(Map<String, Map<String, Map<String, Object>>> continentStructureMap, Map<String, List<Territory>> continentTerritoriesMap_out) {
        Map<String, Territory> ID_territoryMap = new HashMap<>();
        Map<Integer, Territory> color_territoryMap = new HashMap<>();
        Map<String, List<String>> neighborMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Map<String, Object>>> continentEntry : continentStructureMap.entrySet()) {

            List<Territory> continentTerritories = new ArrayList<>();
            for (Map.Entry<String, Map<String, Object>> territoryEntry : continentEntry.getValue().entrySet()) {

                Map<String, Object> territoryFields = territoryEntry.getValue();
                String territoryColor = (String)territoryFields.get("color");
                @SuppressWarnings("unchecked")
                List<Integer> territoryCenterCoords = (List<Integer>)territoryFields.get("coords");
                @SuppressWarnings("unchecked")
                List<String> neighborIDs = (List<String>)territoryFields.get("neighbors");

                Vector2 territoryCenterVector = new Vector2(territoryCenterCoords.get(0), territoryCenterCoords.get(1));

                String territoryID = territoryEntry.getKey();
                Territory territory = new Territory(territoryID, territoryCenterVector);
                territoryID = territoryID.toLowerCase();

                continentTerritories.add(territory);
                ID_territoryMap.put(territoryID, territory);
                color_territoryMap.put(Integer.decode(territoryColor), territory);
                neighborMap.put(territoryID, neighborIDs);
            }
            continentTerritoriesMap_out.put(continentEntry.getKey(), continentTerritories);
        }
        // Parse and set territories' neighbors
        for (Map.Entry<String, List<String>> neighborEntry : neighborMap.entrySet()) {
            List<Territory> neighbors = new ArrayList<>();
            for (String neighborID : neighborEntry.getValue())
                neighbors.add(ID_territoryMap.get(neighborID.toLowerCase()));

            Territory territory = ID_territoryMap.get(neighborEntry.getKey().toLowerCase());
            territory.setNeighbors(neighbors);
        }

        return new TerritoryMap(ID_territoryMap, color_territoryMap);
    }

    private static Map<String, Continent> getContinentMap(Map<String, Integer> continentBonusMap, Map<String, List<Territory>> continentTerritoriesMap) {
        Map<String, Continent> continentMap = new HashMap<>();

        for (Map.Entry<String, Integer> continentEntry : continentBonusMap.entrySet()) {
            String name = continentEntry.getKey();
            List<Territory> territories = continentTerritoriesMap.get(name);
            int bonusTroops = continentBonusMap.get(name);
            continentMap.put(name, new Continent(name, territories, bonusTroops));
        }

        return continentMap;
    }
}
