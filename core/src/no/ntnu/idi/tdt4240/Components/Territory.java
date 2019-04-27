package no.ntnu.idi.tdt4240.Components;

import com.badlogic.gdx.math.Vector2;

public class Territory {
    // Can't start at 0, because the map's borders are black
    private static byte nextColorIndex = 1;

    public final int ID;
    private final Vector2 troopCircleVector;

    public final byte colorIndex;

    private int ownerID;
    private int numTroops;

    // TODO: remove; temporary constructor just to make the code in BoardSystem work
    public String name;
    public Territory(String name) {
        this(0, new Vector2());
        this.name = name;
    }

    public Territory(int ID, Vector2 troopCircleVector) {
        this.ID = ID;
        this.troopCircleVector = troopCircleVector;
        colorIndex = nextColorIndex++;
    }

    public Vector2 getTroopCircleVector() {
        return new Vector2(troopCircleVector);
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
