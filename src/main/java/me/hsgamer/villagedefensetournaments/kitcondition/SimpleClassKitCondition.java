package me.hsgamer.villagedefensetournaments.kitcondition;

import plugily.projects.villagedefense.kits.basekits.Kit;

public class SimpleClassKitCondition extends KitCondition {
    public SimpleClassKitCondition(String value) {
        super(value);
    }

    @Override
    public boolean isKitAllowed(Kit kit) {
        return kit.getClass().getSimpleName().equals(getValue());
    }
}
