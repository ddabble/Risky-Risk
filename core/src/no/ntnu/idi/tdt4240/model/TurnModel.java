package no.ntnu.idi.tdt4240.model;

public class TurnModel {

    private int currentPlayerID;
    private int numberOfPlayers;


    public TurnModel(int currentPlayerID, int numberOfPlayers){
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
