package no.ntnu.idi.tdt4240.controller;

import no.ntnu.idi.tdt4240.data.Territory;

public interface GameViewer {
    void setNumberOfPlayers(int num);
    void territorySelected(Territory t);
    void updateTerritoryTroops(Territory t);
}
