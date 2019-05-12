package no.ntnu.idi.tdt4240.model;

import no.ntnu.idi.tdt4240.controller.IGPGSClient;

public class TurnModel {
    // TODO: set currentPlayerID and numberOfPlayers from somewhere else
    public static final TurnModel INSTANCE = new TurnModel();

    private int currentPlayerID;
    private int numberOfPlayers;
    private IGPGSClient client;

    private TurnModel() {
    }

    public void init(){
        numberOfPlayers = client.getmRiskyTurn().getNumberOfPlayers();
        currentPlayerID = client.getmRiskyTurn().getCurrentPlayer();
    }

    public void setGPGSClient(IGPGSClient client) {
        this.client = client;
    }

    public void nextTurn() {
        currentPlayerID++;
        currentPlayerID%=numberOfPlayers;
    }

    public void setCurrentPlayer(int playerID) {
        currentPlayerID = playerID;
    }

    public int getNumberOfPlayers() {
        if(client != null) {
            return numberOfPlayers;
        } else {
            return 6;
        }
    }

    public int getCurrentPlayerID() {
        return currentPlayerID;
    }
}
