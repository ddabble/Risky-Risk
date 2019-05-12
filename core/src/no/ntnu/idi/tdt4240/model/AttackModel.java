package no.ntnu.idi.tdt4240.model;

import java.util.HashMap;

import no.ntnu.idi.tdt4240.data.Territory;

public class AttackModel {
    public static final AttackModel INSTANCE = new AttackModel();

    private int troopsToPlace;
    private Territory fromTerritory;
    private Territory toTerritory;
    private HashMap<String, Integer> attack;

    private AttackModel() {}

    public void init() {
        attack = new HashMap<>();
    }

    public void setAttackFrom(Territory territory) {
        fromTerritory = territory;
        attack.put(territory.name, territory.getNumTroops());
    }

    public void setAttackTo(Territory territory) {
        System.out.println(attack.size());
        if (attack.size() == 2) {
            attack.remove(toTerritory.name);
        }
        toTerritory = territory;
        attack.put(territory.name, territory.getNumTroops());
    }

    public void setTroopsToPlace(int troopsToPlace) {
        this.troopsToPlace = troopsToPlace;
    }

    public int getTroopsToPlace() {
        return troopsToPlace;
    }

    public Territory getToTerritory() {
        return toTerritory;
    }

    public Territory getFromTerritory() {
        return fromTerritory;
    }

    public HashMap<String, Integer> getAttack() {
        return attack;
    }

    public void cancelAttack() {
        attack.clear();
        fromTerritory = null;
        toTerritory = null;
    }
}
