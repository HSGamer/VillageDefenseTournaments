package me.hsgamer.villagedefensetournaments.arena;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import plugily.projects.villagedefense.kits.KitRegistry;
import plugily.projects.villagedefense.kits.basekits.Kit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class TournamentArena {
    private final String arena;
    private final List<String> kits;
    private final List<UUID> uuids = new ArrayList<>();
    private final AtomicReference<CommandSender> currentHost = new AtomicReference<>();
    private boolean enabled = false;
    private boolean useLockedKit = false;
    private boolean freeForAll = false;
    private boolean stopOnGameEnd = false;
    private boolean allowSpectator = false;

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

    public CommandSender getCurrentHost() {
        return currentHost.get();
    }

    public void setCurrentHost(CommandSender host) {
        this.currentHost.set(host);
    }

    public boolean isAllowSpectator() {
        return allowSpectator;
    }

    public void setAllowSpectator(boolean allowSpectator) {
        this.allowSpectator = allowSpectator;
    }
}
