package no.ntnu.idi.tdt4240;

import android.util.Log;

import java.util.Arrays;

import no.ntnu.idi.tdt4240.controller.IRiskyTurn;
import no.ntnu.idi.tdt4240.model.TurnModel;
import no.ntnu.idi.tdt4240.model.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class RiskyTurn implements IRiskyTurn {
    private static final String TAG = "EBTurn";

    public byte[] data;
    public int turnCounter;

    private int numPlayers;
    private int currentPlayer;

    // This is the byte array we will write out to the TBMP API.
    //the way i do it now i just always store the byte array so i don't have to parse TerritoryMap here
    //the advantage of doing it this way is that RiskyTurn does not need a reference to TerritoryMap -Ø
    public byte[] persist() {
        return data;
    }

    // Creates a new instance of RiskyTurn.
    public static RiskyTurn unpersist(byte[] byteArray) {
        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.");
            return new RiskyTurn();
        }

        RiskyTurn riskyTurn = new RiskyTurn();
        riskyTurn.data = byteArray;
        if (byteArray.length == 1) {
            Log.d(TAG, "Initial setup data was transferred, this game has " + byteArray[0] + " players");
            riskyTurn.numPlayers = byteArray[0];
            riskyTurn.currentPlayer = TurnModel.INSTANCE.getCurrentPlayerID();
        } else { // not initial setup, so just read last two bytes
            riskyTurn.numPlayers = byteArray[byteArray.length - 1];
            riskyTurn.currentPlayer = byteArray[byteArray.length - 2];
        }
        System.out.println("Byte array received looks like this:");
        System.out.println(Arrays.toString(byteArray));
        return riskyTurn;
    }

    @Override
    public void updateData(TerritoryMap map, int currentPlayer) {
        int index = 0;
        //this technically only needs to happen for the first player
        data = new byte[map.getAllTerritories().size() * 2 + 2];
        for (Territory territory : map.getAllTerritories()) {
            data[index] = (byte)territory.getNumTroops();
            data[index + 1] = (byte)territory.getOwnerID();
            index += 2;
        }
        //store whose turn it is and how many people are playing
        data[map.getAllTerritories().size() * 2] = (byte)currentPlayer;
        data[map.getAllTerritories().size() * 2 + 1] = (byte)numPlayers;
    }

    @Override
    public int getNumPlayers() {
        return numPlayers;
    }

    @Override
    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    @Override
    public void persistNumPlayers() {
        data = new byte[1];
        data[0] = (byte)numPlayers;
    }

    @Override
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    //this gets a reference to TerritoryMap and it just changes that reference, so no need to return a value ;^)
    @Override
    public void getTerritoryMapData(TerritoryMap map) {
        int index = 0;
        for (Territory territory : map.getAllTerritories()) {
            territory.setNumTroops(data[index]);
            territory.setOwnerID(data[index + 1]);
            index += 2;
        }
    }

    @Override
    public boolean isDataInitialized() {
        return data != null && data.length != 0 && data.length != 1;
    }

    @Override
    public int getTurnCounter() {
        return turnCounter;
    }
}
