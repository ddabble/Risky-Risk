package no.ntnu.idi.tdt4240.Components;

import com.badlogic.gdx.math.Vector2;

public class Territory {
    public int Owner;
    public int Troops;
    public Vector2 troopCircleVector;
    public final int ID;

    public Territory(int ID, Vector2 troopCircleVector) {
        this.ID = ID;
        this.troopCircleVector = troopCircleVector;
    }

    public void setTroops(int troops) {
        Troops = troops;
    }

    public int getTroops() {
        return Troops;
    }

    public void setOwner(int owner) {
        Owner = owner;
    }

    public int getOwner() {
        return Owner;
    }
}
