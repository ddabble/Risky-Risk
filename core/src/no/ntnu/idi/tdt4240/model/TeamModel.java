package no.ntnu.idi.tdt4240.model;

import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;


public class TeamModel {
    private static int teamCount = 0;
    private List<Territory> territories;
    public final int ID;

    public TeamModel(){
        ID = ++teamCount;
    }
    public int getNumOfTerritories() {
        return territories.size();
    }

    public void loseTerritory(Territory territory){
        territories.remove(territory);
    }

    public void addTerritory(Territory territory){
        territories.add(territory);
    }


    public boolean hasLost(){
        return territories.size() <= 0;
    }
    public static int getTeamCount(){
        return teamCount;
    }

}
