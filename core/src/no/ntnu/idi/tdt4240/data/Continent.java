package no.ntnu.idi.tdt4240.data;

import java.util.List;

public class Continent {
    public final String name;
    private final List<Territory> territories;
    private int bonusTroops = 0;

    public Continent(String name, List<Territory> territories) {
        this.name = name;
        this.territories = territories;
    }

    public void setBonusTroops(int bonusTroops) {
        this.bonusTroops = bonusTroops;
    }

    public int getBonusTroops() {
        return bonusTroops;
    }

    public List<Territory> getTerritories() {
        return territories;
    }
}
