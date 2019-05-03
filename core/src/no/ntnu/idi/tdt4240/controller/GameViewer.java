package no.ntnu.idi.tdt4240.controller;

import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;

public interface GameViewer {
    void setNumberOfPlayers(int num);
    void territorySelected(Territory t);
    void updateTerritoryTroops(Territory t);
    void initializeBoard(List<Territory> territories);
}
