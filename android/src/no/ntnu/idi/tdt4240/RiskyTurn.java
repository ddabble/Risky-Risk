package no.ntnu.idi.tdt4240;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import no.ntnu.idi.tdt4240.controller.IRiskyTurn;
import no.ntnu.idi.tdt4240.data.Territory;
import no.ntnu.idi.tdt4240.util.TerritoryMap;

public class RiskyTurn implements IRiskyTurn {

    public static final String TAG = "EBTurn";

    public byte[] data;
    public int turnCounter;
    public int numberOfPlayers;
    public int currentPlayer;

    public RiskyTurn() {
    }

    // This is the byte array we will write out to the TBMP API.
    //the way i do it now i just always store the byte array so i don't have to parse TerritoryMap here
    //the advantage of doing it this way is that RiskyTurn does not need a reference to TerritoryMap -Ø
    public byte[] persist() {
        return data;
    }

    // Creates a new instance of RiskyTurn.
    static public RiskyTurn unpersist(byte[] byteArray) {

        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.");
            return new RiskyTurn();
        }

        RiskyTurn riskyTurn = new RiskyTurn();
        riskyTurn.data = byteArray;
        if(byteArray.length == 1) {
            Log.d(TAG, "Initial setup data was transfered, this game has " + byteArray[0] + " players");
            riskyTurn.numberOfPlayers = byteArray[0];
        } else { // not initial setup, so just read last two bytes
            riskyTurn.currentPlayer = byteArray[byteArray.length-2];
            riskyTurn.numberOfPlayers = byteArray[byteArray.length-1];
        }
        return riskyTurn;
    }

    public void updateData(TerritoryMap map, int currentPlayer) {
        int index = 0;
        //this technically only needs to happen for the first player
        data = new byte[map.getAllTerritories().size()*2+2];
        for (Territory territory : map.getAllTerritories()){
            data[index] = (byte)territory.getNumTroops();
            data[index+1] = (byte)territory.getOwnerID();
            index+=2;
        }
        //store whos turn it is and how many people are playing
        data[map.getAllTerritories().size()*2] = (byte)currentPlayer;
        data[map.getAllTerritories().size()*2+1] = (byte)numberOfPlayers;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void persistNumberOfPlayers() {
        data = new byte[1];
        data[0] = (byte)numberOfPlayers;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    //is this even used -Ø
   // public String getTurnData() {
   //     return data;
    //}

    //this gets a reference to TerritoryMap and it just changes that reference, so no need to return a value ;^)
    public void getTerritoryMapData(TerritoryMap map) {
        int index = 0;
        for (Territory territory : map.getAllTerritories()){
            territory.setNumTroops(data[index]);
            territory.setOwnerID(data[index+1]);
            index+=2;
        }
    }

    public boolean isDataInitialized() {
        return data != null && data.length != 0 && data.length != 1;
    }

    public int getTurnCounter() {
        return turnCounter;
    }
}