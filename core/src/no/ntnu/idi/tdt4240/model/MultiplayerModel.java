package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class MultiplayerModel {
    public static final MultiplayerModel INSTANCE = new MultiplayerModel();

    private static final int[] COLORS = new int[] {0xFE796F, 0xFECFFF, 0xF4FE6F, 0xACFE6F, 0x6FFEC1, 0xAB6FFE, 0xFE6FC2, 0xFEBB6F};

    private int numPlayers;
    private List<Integer> playerIDs;

    private Map<Integer, Color> playerID_colorMap;
    private Map<Integer, Set<Territory>> playerID_territoriesMap; // functions as the leaderboard

    private MultiplayerModel() {}

    public Color getPlayerColor(int playerID) {
        return playerID_colorMap.get(playerID);
    }

    public Map<Integer, Color> getPlayerID_colorMap() {
        return new HashMap<>(playerID_colorMap);
    }

    /**
     * @return an <i>unmodifiable</i> set of the player's owned territories.
     */
    public Set<Territory> getTerritoriesOwnedByPlayer(int playerID) {
        return Collections.unmodifiableSet(playerID_territoriesMap.get(playerID));
    }

    /**
     * @return a map from player IDs to an <i>unmodifiable</i> set of the player's owned territories.
     */
    public Map<Integer, Set<Territory>> getTerritoriesPerPlayer() {
        Map<Integer, Set<Territory>> newMap = new HashMap<>(playerID_territoriesMap.size());

        for (Map.Entry<Integer, Set<Territory>> entry : playerID_territoriesMap.entrySet())
            newMap.put(entry.getKey(), Collections.unmodifiableSet(entry.getValue()));

        return newMap;
    }

    public void onTerritoryChangedOwner(int oldPlayerID, int newPlayerID, Territory territory) {
        playerID_territoriesMap.get(oldPlayerID).remove(territory);
        playerID_territoriesMap.get(newPlayerID).add(territory);
    }

    public static void init(int numPlayers) {
        INSTANCE._init(numPlayers);
        TurnModel.init(INSTANCE.playerIDs);
    }

    private void _init(int numPlayers) {
        if (numPlayers > COLORS.length)
            throw new IllegalArgumentException("Number of players can't be greater than the number of defined colors!");

        this.numPlayers = numPlayers;
        System.out.println("Number of players in MultiplayerModel: " + numPlayers);
        System.out.println("Number of players in GPGSclient: " + TurnModel.INSTANCE.getNumberOfPlayers());

        playerIDs = generatePlayerIDs();
        assignPlayerColors();
        assignTerritoryOwners(TerritoryModel.getTerritoryMap());
        initLeaderboard();
    }

    private List<Integer> generatePlayerIDs() {
        List<Integer> playerIDs = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++)
            playerIDs.add(i);
        Collections.shuffle(playerIDs);
        return playerIDs;
    }

    private void assignPlayerColors() {
        playerID_colorMap = new HashMap<>(playerIDs.size());
        List<Color> colors = intArrayToColorList(COLORS);
        for (int i = 0; i < playerIDs.size(); i++)
            playerID_colorMap.put(playerIDs.get(i), colors.get(i));
    }

    private static List<Color> intArrayToColorList(int[] colorArray) {
        List<Color> colorList = new ArrayList<>(colorArray.length);
        for (int rgbColor : colorArray) {
            int rgbaColor = (rgbColor << 8) | 0xFF;
            colorList.add(new Color(rgbaColor));
        }
        Collections.shuffle(colorList);
        return colorList;
    }

    private void assignTerritoryOwners(TerritoryMap territoryMap) {
        List<Territory> territories = territoryMap.getAllTerritories();
        final int numTerritories = territories.size();

        // Fill the list with an approximately equal amount of each player ID, then shuffle the list
        List<Integer> playerIDsForTerritories = new ArrayList<>(numTerritories);
        for (int i = 0; i < numTerritories; i++)
            playerIDsForTerritories.add(playerIDs.get(i % numPlayers));
        Collections.shuffle(playerIDsForTerritories);

        for (int i = 0; i < numTerritories; i++) {
            territories.get(i).setOwnerID(playerIDsForTerritories.get(i));
            territories.get(i).setNumTroops(1);
        }
    }

    private void initLeaderboard() {
        playerID_territoriesMap = new HashMap<>(numPlayers);

        List<Territory> territories = TerritoryModel.getTerritoryMap().getAllTerritories();
        for (Territory territory : territories) {
            Set<Territory> ownerTerritories = playerID_territoriesMap.get(territory.getOwnerID());
            if (ownerTerritories != null)
                ownerTerritories.add(territory);
            else {
                final int approxNumTerritoriesPerPlayer = MathUtils.ceilPositive(territories.size() / (float)numPlayers);
                ownerTerritories = new HashSet<>(approxNumTerritoriesPerPlayer);
                ownerTerritories.add(territory);
                playerID_territoriesMap.put(territory.getOwnerID(), ownerTerritories);
            }
        }
    }

}
