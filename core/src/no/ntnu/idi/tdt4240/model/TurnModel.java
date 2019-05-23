package no.ntnu.idi.tdt4240.model;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;

public class TurnModel {
    public static final TurnModel INSTANCE = new TurnModel();

    private int currentPlayerID;
    private int numberOfPlayers;
    private IGPGSClient client;

    private TurnModel() {}

    public static void init() {
        INSTANCE._init();
    }

    private void _init() {
        try {
            numberOfPlayers = client.getmRiskyTurn().getNumberOfPlayers();
            currentPlayerID = client.getmRiskyTurn().getCurrentPlayer();
        } catch (NullPointerException e) {
            numberOfPlayers = 2;
            currentPlayerID = 0;
        }
    }

    public void setGPGSClient(IGPGSClient client) {
        this.client = client;
    }

    public void nextTurn() {
        currentPlayerID++;
        currentPlayerID %= numberOfPlayers;
    }

    public void setCurrentPlayer(int playerID) {
        currentPlayerID = playerID;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }
}
