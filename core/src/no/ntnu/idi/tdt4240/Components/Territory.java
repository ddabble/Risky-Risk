package no.ntnu.idi.tdt4240.Components;

public class Territory {
    public int Owner;
    public int Troops;
    private int ID;

    public Territory(int ID){
        this.ID = ID;
    }

    public int getID() {
        return ID;
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
