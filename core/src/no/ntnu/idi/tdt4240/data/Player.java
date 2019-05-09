package no.ntnu.idi.tdt4240.data;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private int troopsToPlace;
    private Territory fromTerritory;
    private Territory toTerritory;
    private HashMap<String, Integer> attack;

    public Player(){
        attack = new HashMap<>();
    }

    public void setAttackFrom(Territory territory){
        fromTerritory = territory;
        attack.put(territory.name, territory.getNumTroops());
    }

    public void setAttackTo(Territory territory){
        toTerritory = territory;
        attack.put(territory.name, territory.getNumTroops());
    }

    public void setTroopsToPlace(int troopsToPlace){
        this.troopsToPlace = troopsToPlace;
    }
    public int getTroopsToPlace(){
        return troopsToPlace;
    }
    public Territory getToTerritory(){ return toTerritory; }
    public Territory getFromTerritory(){ return fromTerritory; }
    public HashMap<String, Integer> getAttack(){ return attack; }
    public void cancelAttack(){ attack.clear(); }
}
