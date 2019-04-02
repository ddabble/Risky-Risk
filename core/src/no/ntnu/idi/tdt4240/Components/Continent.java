package no.ntnu.idi.tdt4240.Components;

public class Continent {

    public int Owner;
    public Integer[] Territories;
    private int ID;

    public Continent(int ID, Integer[] Territories) {
        this.ID = ID;
        this.Territories = Territories;
    }

    public void setOwner(int owner) {
        Owner = owner;
    }

    public int getOwner() {
        return Owner;
    }

    public Integer[] getTerritories() {
        return Territories;
    }

    public int getID() {
        return ID;
    }
}
