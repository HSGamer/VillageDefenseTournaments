package me.hsgamer.villagedefensetournaments.builder;

import me.hsgamer.hscore.builder.Builder;
import me.hsgamer.villagedefensetournaments.kitcondition.ClassKitCondition;
import me.hsgamer.villagedefensetournaments.kitcondition.KitCondition;
import me.hsgamer.villagedefensetournaments.kitcondition.NameKitCondition;
import me.hsgamer.villagedefensetournaments.kitcondition.SimpleClassKitCondition;

import java.util.List;
import java.util.stream.Collectors;

public class KitConditionBuilder extends Builder<String, KitCondition> {
    public static final KitConditionBuilder INSTANCE = new KitConditionBuilder();

    private KitConditionBuilder() {
        register(NameKitCondition::new, "name", "display-name");
        register(ClassKitCondition::new, "class");
        register(SimpleClassKitCondition::new, "simple-class", "simple");
    }

    public List<KitCondition> getKitConditions(List<String> list) {
        return list.stream()
                .map(string -> {
                    String[] split = string.split(":", 2);
                    String name = split[0].trim();
                    String value = (split.length > 1 ? split[1] : "").trim();
                    return build(name, value).orElseGet(() -> new NameKitCondition(string.trim()));
                })
                .collect(Collectors.toList());
    }
}
