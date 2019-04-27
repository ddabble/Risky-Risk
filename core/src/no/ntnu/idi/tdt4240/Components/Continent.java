package no.ntnu.idi.tdt4240.Components;

import java.util.List;

public class Continent {

    public final String name;
    private final List<Territory> territories;

    public Continent(String name, List<Territory> territories) {
        this.name = name;
        this.territories = territories;
    }

    public List<Territory> getTerritories() {
        return territories;
    }
}
