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

    public RiskyTurn() {
    }

    // This is the byte array we will write out to the TBMP API.
    //the way i do it now i just always store the byte array so i don't have to parse TerritoryMap here
    //the advantage of doing it this way is that RiskyTurn does not need a reference to TerritoryMap -Ø
    public byte[] persist() {
        return data;
    }

    // Creates a new instance of SkeletonTurn.
    static public RiskyTurn unpersist(byte[] byteArray) {

        if (byteArray == null) {
            Log.d(TAG, "Empty array---possible bug.");
            return new RiskyTurn();
        }

        RiskyTurn riskyTurn = new RiskyTurn();
        riskyTurn.data = byteArray;
        return riskyTurn;
    }

    public void updateData(TerritoryMap map) {
        int index = 0;
        //this technically only needs to happen for the first player
        data = new byte[map.getAllTerritories().size()];
        for (Territory territory : map.getAllTerritories()){
            data[index] = (byte)territory.getNumTroops();
            index++;
        }
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
            index++;
        }
    }

    public int getTurnCounter() {
        return turnCounter;
    }
}