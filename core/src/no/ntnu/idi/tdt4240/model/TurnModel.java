package no.ntnu.idi.tdt4240.model;

public class TurnModel {
    // TODO: set currentPlayerID and numberOfPlayers from somewhere else
    public static final TurnModel INSTANCE = new TurnModel(0, 8);

    private int currentPlayerID;
    private int numberOfPlayers;


    private TurnModel(int currentPlayerID, int numberOfPlayers){
        this.currentPlayerID = currentPlayerID;
        this.numberOfPlayers = numberOfPlayers;
    }

    public void takeTurn(){
        currentPlayerID++;
        if (currentPlayerID >= numberOfPlayers){
            currentPlayerID = 0;
        }
    }

    public int getCurrentPlayerID(){
        return currentPlayerID;
    }

}
