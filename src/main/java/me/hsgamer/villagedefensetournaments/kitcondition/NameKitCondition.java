package me.hsgamer.villagedefensetournaments.kitcondition;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import plugily.projects.villagedefense.kits.basekits.Kit;

public class NameKitCondition extends KitCondition {
    public NameKitCondition(String value) {
        super(MessageUtils.colorize(value));
    }

    @Override
    public boolean isKitAllowed(Kit kit) {
        return kit.getName().equalsIgnoreCase(getValue());
    }
}
