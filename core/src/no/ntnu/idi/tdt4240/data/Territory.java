package no.ntnu.idi.tdt4240.data;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Territory {
    // Can't start at 0, because the map's borders are black
    private static byte nextColorIndex = 1;

    public final String name; // not really needed; only for debugging
    private final Vector2 troopCircleVector;

    public final byte colorIndex;

    private List<Territory> neighbors;

    private int ownerID;
    private int numTroops;

    public Territory(String name, Vector2 troopCircleVector) {
        this.name = name;
        this.troopCircleVector = troopCircleVector;
        colorIndex = nextColorIndex++;
    }

    public Vector2 getTroopCircleVector() {
        return troopCircleVector.cpy();
    }

    public List<Territory> getNeighbors() {
        return new ArrayList<>(neighbors);
    }

    public void setNeighbors(List<Territory> neighbors) {
        this.neighbors = new ArrayList<>(neighbors);
    }

    public int getNumTroops() {
        return numTroops;
    }

    public void setNumTroops(int numTroops) {
        this.numTroops = numTroops;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }
}
