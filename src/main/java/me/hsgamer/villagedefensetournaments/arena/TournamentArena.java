package me.hsgamer.villagedefensetournaments.arena;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import plugily.projects.villagedefense.kits.KitRegistry;
import plugily.projects.villagedefense.kits.basekits.Kit;

import java.util.*;

public class TournamentArena {
    private final String arena;
    private final List<String> kits;
    private final List<UUID> uuids = new ArrayList<>();
    private boolean enabled = false;
    private boolean useLockedKit = false;
    private boolean freeForAll = false;
    private boolean stopOnGameEnd = false;

    public TournamentArena(String arena, List<String> kits) {
        this.arena = arena;
        this.kits = kits;
    }

    public String getArena() {
        return arena;
    }

    public Optional<Kit> parseDefaultKit() {
        if (kits.isEmpty()) {
            return Optional.empty();
        }
        String colored = MessageUtils.colorize(kits.get(0));
        return KitRegistry.getKits().parallelStream().filter(kit -> colored.equalsIgnoreCase(kit.getName())).findFirst();
    }

    public List<String> getKits() {
        return kits;
    }

    public boolean isKitAllowed(Kit kit) {
        if (kits.isEmpty()) {
            return true;
        }
        List<String> colored = new ArrayList<>(kits);
        colored.replaceAll(MessageUtils::colorize);
        return colored.parallelStream().anyMatch(s -> kit.getName().equalsIgnoreCase(s));
    }

    public List<UUID> getUuids() {
        return Collections.unmodifiableList(uuids);
    }

    public void addUUID(UUID uuid) {
        uuids.add(uuid);
    }

    public void addPlayer(OfflinePlayer player) {
        addUUID(player.getUniqueId());
    }

    public void clearAllUuids() {
        uuids.clear();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isUseLockedKit() {
        return useLockedKit;
    }

    public void setUseLockedKit(boolean useLockedKit) {
        this.useLockedKit = useLockedKit;
    }

    public boolean isFreeForAll() {
        return freeForAll;
    }

    public void setFreeForAll(boolean freeForAll) {
        this.freeForAll = freeForAll;
    }

    public boolean isStopOnGameEnd() {
        return stopOnGameEnd;
    }

    public void setStopOnGameEnd(boolean stopOnGameEnd) {
        this.stopOnGameEnd = stopOnGameEnd;
    }
}
