package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.util.TerritoryMap;

public interface IRiskyTurn {
    void getTerritoryMapData(TerritoryMap map);
    int getTurnCounter();
    void updateData(TerritoryMap map);
}
