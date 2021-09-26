package me.hsgamer.villagedefensetournaments.kitcondition;

import plugily.projects.villagedefense.kits.basekits.Kit;

public abstract class KitCondition {
    private final String value;

    protected KitCondition(String value) {
        this.value = value;
    }

    public abstract boolean isKitAllowed(Kit kit);

    public String getValue() {
        return value;
    }
}
