package no.ntnu.idi.tdt4240.model;

import java.util.List;

public class TurnModel {
    public static final TurnModel INSTANCE = new TurnModel();

    private List<Integer> playerIDs;
    private int currentPlayerIndex;

    private TurnModel() {}

    static void init(List<Integer> playerIDs) {
        INSTANCE._init(playerIDs);
    }

    private void _init(List<Integer> playerIDs) {
        this.playerIDs = playerIDs;
        currentPlayerIndex = 0;
    }

    public void nextTurn() {
        currentPlayerIndex++;
        currentPlayerIndex %= playerIDs.size();
    }

    public void removePlayer(Integer playerID) {
        playerIDs.remove(playerID);
    }

    public int getNumPlayingPlayers() {
        return playerIDs.size();
    }

    public int getCurrentPlayerID() {
        return playerIDs.get(currentPlayerIndex);
    }
}
