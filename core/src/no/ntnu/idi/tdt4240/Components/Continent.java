package no.ntnu.idi.tdt4240.Components;

public class Continent {
    public Integer[] Territories;
    private int ID;

    public Continent(int ID, Integer[] Territories) {
        this.ID = ID;
        this.Territories = Territories;
    }

    public Integer[] getTerritories() {
        return Territories;
    }

    public int getID() {
        return ID;
    }
}
