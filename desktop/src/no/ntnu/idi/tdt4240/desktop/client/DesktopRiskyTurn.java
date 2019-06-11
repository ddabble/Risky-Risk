package no.ntnu.idi.tdt4240.desktop.client;

import no.ntnu.idi.tdt4240.client.IRiskyTurn;
import no.ntnu.idi.tdt4240.util.TerritoryMap;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DesktopRiskyTurn implements IRiskyTurn {
    private int numPlayers;

    @Override
    public void getTerritoryMapData(TerritoryMap map) {
        throw new NotImplementedException();
    }

    @Override
    public int getTurnCounter() {
        throw new NotImplementedException();
    }

    @Override
    public void updateData(TerritoryMap map, int currentPlayer) {
        throw new NotImplementedException();
    }

    @Override
    public boolean isDataInitialized() {
        throw new NotImplementedException();
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
    public int getCurrentPlayer() {
        throw new NotImplementedException();
    }

    @Override
    public void persistNumPlayers() {
        throw new NotImplementedException();
    }
}
