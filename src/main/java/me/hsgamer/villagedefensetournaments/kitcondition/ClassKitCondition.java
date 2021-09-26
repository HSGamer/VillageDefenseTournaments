package me.hsgamer.villagedefensetournaments.kitcondition;

import plugily.projects.villagedefense.kits.basekits.Kit;

public class ClassKitCondition extends KitCondition {
    public ClassKitCondition(String value) {
        super(value);
    }

    @Override
    public boolean isKitAllowed(Kit kit) {
        return kit.getClass().getName().equals(getValue());
    }
}
