package no.ntnu.idi.tdt4240.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class PlayerModel {
    public static final PlayerModel INSTANCE = new PlayerModel(8);

    private static final int[] COLORS = new int[] {0xFE796F, 0xFECFFF, 0xF4FE6F, 0xACFE6F, 0x6FFEC1, 0xAB6FFE, 0xFE6FC2, 0xFEBB6F};

    private int numPlayers;
    private Map<Integer, Integer> playerID_colorMap;

    public Map<Integer, Integer> getPlayerID_colorMap() {
        return new HashMap<>(playerID_colorMap);
    }

    private PlayerModel(int numPlayers) {
        if (numPlayers > COLORS.length)
            throw new IllegalArgumentException("Number of players can't be greater than the number of defined colors!");

        this.numPlayers = numPlayers;
        List<Integer> playerIDs = generatePlayerIDs();
        assignPlayerColors(playerIDs);
    }

    public void init() {
        List<Integer> playerIDs = new ArrayList<>(playerID_colorMap.keySet());
        assignTerritoryOwners(playerIDs, TerritoryModel.getTerritoryMap());
    }

    private List<Integer> generatePlayerIDs() {
        List<Integer> playerIDs = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++)
            playerIDs.add(i);
        Collections.shuffle(playerIDs);
        return playerIDs;
    }

    private void assignPlayerColors(List<Integer> playerIDs) {
        playerID_colorMap = new HashMap<>();
        for (int i = 0; i < playerIDs.size(); i++)
            playerID_colorMap.put(playerIDs.get(i), COLORS[i]);
    }

    private void assignTerritoryOwners(List<Integer> playerIDs, TerritoryMap territoryMap) {
        List<Territory> territories = territoryMap.getAllTerritories();
        final int numTerritories = territories.size();

        // Fill the list with an approximately equal amount of each player ID, then shuffle the list
        List<Integer> playerIDsForTerritories = new ArrayList<>(numTerritories);
        for (int i = 0; i < numTerritories; i++)
            playerIDsForTerritories.add(playerIDs.get(i % numPlayers));
        Collections.shuffle(playerIDsForTerritories);

        for (int i = 0; i < numTerritories; i++)
            territories.get(i).setOwnerID(playerIDsForTerritories.get(i));
    }

    public int getPlayerColor(int playerID) {
        return playerID_colorMap.get(playerID);
    }
}
