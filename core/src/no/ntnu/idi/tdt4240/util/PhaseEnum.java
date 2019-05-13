package no.ntnu.idi.tdt4240.util;

public enum PhaseEnum {
    PLACE("Place"),
    ATTACK("Attack"),
    FORTIFY("Fortify");

    private final String name;

    PhaseEnum(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
