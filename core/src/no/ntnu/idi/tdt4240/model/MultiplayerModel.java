package no.ntnu.idi.tdt4240.model;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class MultiplayerModel {
    public static final MultiplayerModel INSTANCE = new MultiplayerModel();

    private static final int[] COLORS = new int[] {0xFE796F, 0xFECFFF, 0xF4FE6F, 0xACFE6F, 0x6FFEC1, 0xAB6FFE, 0xFE6FC2, 0xFEBB6F};

    private int numPlayers;
    private Map<Integer, Color> playerID_colorMap;
    private Map<Integer, Integer> playerID_numTerritories; // same as leaderboard

    private MultiplayerModel() {}

    public Color getPlayerColor(int playerID) {
        return playerID_colorMap.get(playerID);
    }

    public Map<Integer, Color> getPlayerID_colorMap() {
        return new HashMap<>(playerID_colorMap);
    }

    public void init(int numPlayers) {
        if (numPlayers > COLORS.length)
            throw new IllegalArgumentException("Number of players can't be greater than the number of defined colors!");

        this.numPlayers = numPlayers;
        System.out.println("Number of players in MultiplayerModel: " + numPlayers);
        System.out.println("Number of players in GPGSclient: " + BoardModel.INSTANCE.getNumberOfPlayers());
        List<Integer> playerIDs = generatePlayerIDs();
        assignPlayerColors(playerIDs);
        assignTerritoryOwners(playerIDs, TerritoryModel.getTerritoryMap());
        initLeaderboard();
    }

    private List<Integer> generatePlayerIDs() {
        List<Integer> playerIDs = new ArrayList<>(numPlayers);
        for (int i = 0; i < numPlayers; i++)
            playerIDs.add(i);
        Collections.shuffle(playerIDs);
        return playerIDs;
    }

    private void assignPlayerColors(List<Integer> playerIDs) {
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

    private void assignTerritoryOwners(List<Integer> playerIDs, TerritoryMap territoryMap) {
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
        List<Territory> territories = TerritoryModel.getTerritoryMap().getAllTerritories();
        int[] numOfTerritories = new int[numPlayers];

        for (Territory t : territories)
            numOfTerritories[t.getOwnerID()] += 1;

        Map<Integer, Integer> leaderboard = new HashMap<>();
        for (int i = 0; i < numPlayers; i++) {
            leaderboard.put(i, numOfTerritories[i]);
        }
        setLeaderboard(leaderboard);
    }

    public Map<Integer, Integer> getLeaderboard() {
        return playerID_numTerritories;
    }

    public void setLeaderboard(Map<Integer, Integer> playerID_numTerritories) {
        this.playerID_numTerritories = playerID_numTerritories;
    }

}
