package no.ntnu.idi.tdt4240.client;

import no.ntnu.idi.tdt4240.util.TerritoryMap;

public interface IRiskyTurn {
    //reads the data held in the RiskyTurn object into TerritoryMap map
    void getTerritoryMapData(TerritoryMap map);

    int getTurnCounter();

    //updates the data held in the RiskyTurn object
    void updateData(TerritoryMap map, int currentPlayer);

    boolean isDataInitialized();

    int getNumPlayers();

    void setNumPlayers(int numPlayers);

    int getCurrentPlayer();

    void persistNumPlayers();
}
