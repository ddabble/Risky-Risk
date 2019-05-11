package no.ntnu.idi.tdt4240.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Continent;
import no.ntnu.idi.tdt4240.data.Territory;

public class TerritoryMap {
    private Map<String, Territory> IDmap;
    private Map<Integer, String> color_IDmap;

    // TODO: move to separate class
    public Map<String, Continent> continentMap;

    public TerritoryMap(Map<String, Territory> IDmap, Map<Integer, String> color_IDmap) {
        this.IDmap = IDmap;
        this.color_IDmap = color_IDmap;
    }

    /**
     * @param ID a territory's lowercase ID
     */
    public Territory getTerritory(String ID) {
        return IDmap.get(ID);
    }

    public Territory getTerritory(int color) {
        return IDmap.get(getID(color));
    }

    public List<Territory> getAllTerritories() {
        return new ArrayList<>(IDmap.values());
    }

    public List<Continent> getAllContinents() {
        return new ArrayList<>(continentMap.values());
    }

    public String getID(int color) {
        return color_IDmap.get(color);
    }

    public Map<String, Territory> getIDmap() {
        return new HashMap<>(IDmap);
    }

    public Map<Integer, String> getColor_IDmap() {
        return new HashMap<>(color_IDmap);
    }

    public void setColor_IDmap(Map<Integer, String> color_IDmap) {
        this.color_IDmap = new HashMap<>(color_IDmap);
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
        Map<String, Territory> IDmap = new HashMap<>();
        Map<Integer, String> color_IDmap = new HashMap<>();
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
                IDmap.put(territoryID, territory);
                color_IDmap.put(Integer.decode(territoryColor), territoryID);
                neighborMap.put(territoryID, neighborIDs);
            }
            continentTerritoriesMap_out.put(continentEntry.getKey(), continentTerritories);
        }
        // Parse and set territories' neighbors
        for (Map.Entry<String, List<String>> neighborEntry : neighborMap.entrySet()) {
            List<Territory> neighbors = new ArrayList<>();
            for (String neighborID : neighborEntry.getValue())
                neighbors.add(IDmap.get(neighborID.toLowerCase()));

            Territory territory = IDmap.get(neighborEntry.getKey().toLowerCase());
            territory.setNeighbors(neighbors);
        }

        return new TerritoryMap(IDmap, color_IDmap);
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
