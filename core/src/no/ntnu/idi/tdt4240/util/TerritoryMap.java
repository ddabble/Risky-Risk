package no.ntnu.idi.tdt4240.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.Components.Continent;
import no.ntnu.idi.tdt4240.Components.Territory;

public class TerritoryMap {
    private Map<String, Territory> IDmap;
    private Map<Integer, String> color_IDmap;

    // TODO: move to separate class
    public List<Continent> continents;

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

    public static TerritoryMap parseJsonMapStructure(File jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        Map readJson;
        try {
            readJson = mapper.readValue(jsonFile, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        @SuppressWarnings("unchecked")
        Map<String, Map<String, Map<String, Object>>> continentMap = readJson;

        List<Continent> continents = new ArrayList<>();
        Map<String, Territory> IDmap = new HashMap<>();
        Map<Integer, String> color_IDmap = new HashMap<>();
        for (Map.Entry<String, Map<String, Map<String, Object>>> continentEntry : continentMap.entrySet()) {

            List<Territory> continentTerritories = new ArrayList<>();
            for (Map.Entry<String, Map<String, Object>> territoryEntry : continentEntry.getValue().entrySet()) {

                String territoryID = territoryEntry.getKey();
                Territory territory = new Territory(territoryID);
                territoryID = territoryID.toLowerCase();

                continentTerritories.add(territory);
                IDmap.put(territoryID, territory);

                Map<String, Object> territoryFields = territoryEntry.getValue();
                String territoryColor = (String)territoryFields.get("color");
                color_IDmap.put(Integer.decode(territoryColor), territoryID);
            }
            continents.add(new Continent(continentEntry.getKey(), continentTerritories));
        }
        // Parse and set territories' neighbors
        for (Map.Entry<String, Map<String, Map<String, Object>>> continentEntry : continentMap.entrySet()) {
            for (Map.Entry<String, Map<String, Object>> territoryEntry : continentEntry.getValue().entrySet()) {

                Map<String, Object> territoryFields = territoryEntry.getValue();
                @SuppressWarnings("unchecked")
                List<String> neighborIDs = (List<String>)territoryFields.get("neighbors");

                List<Territory> neighbors = new ArrayList<>();
                for (String neighborID : neighborIDs) {
                    neighbors.add(IDmap.get(neighborID));
                }

                Territory territory = IDmap.get(territoryEntry.getKey().toLowerCase());
                territory.setNeighbors(neighbors);
            }
        }

        TerritoryMap territoryMap = new TerritoryMap(IDmap, color_IDmap);
        territoryMap.continents = continents;
        return territoryMap;
    }
}
