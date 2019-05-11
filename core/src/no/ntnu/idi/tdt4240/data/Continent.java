package no.ntnu.idi.tdt4240.data;

import java.util.List;

public class Continent {
    public final String name;
    private final List<Territory> territories;

    private final int bonusTroops;

    public Continent(String name, List<Territory> territories, int bonusTroops) {
        this.name = name;
        this.territories = territories;
        this.bonusTroops = bonusTroops;
    }

    public int getBonusTroops() {
        return bonusTroops;
    }

    public List<Territory> getTerritories() {
        return territories;
    }
}
