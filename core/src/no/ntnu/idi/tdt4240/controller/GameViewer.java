package no.ntnu.idi.tdt4240.controller;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

import no.ntnu.idi.tdt4240.data.Territory;

public interface GameViewer {
    void setNumberOfPlayers(int num);
    void territorySelected(Territory t);
    void updateTerritoryTroops(Territory t);
    void initializeBoard(List<Territory> territories);
    void setPlayerColorLookup(float[] playerColorLookup);
    void setMapSprite(Sprite mapSprite);
    void updatePhase(String curPhase, String nextPhase);
}
