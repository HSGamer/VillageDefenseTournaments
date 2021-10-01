package me.hsgamer.villagedefensetournaments.arena;

import me.hsgamer.villagedefensetournaments.kitcondition.KitCondition;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import plugily.projects.villagedefense.kits.KitRegistry;
import plugily.projects.villagedefense.kits.basekits.Kit;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class TournamentArena {
    private final String arena;
    private final List<KitCondition> kitConditions;
    private final List<UUID> players = new ArrayList<>();
    private final List<UUID> spectators = new ArrayList<>();
    private final AtomicReference<CommandSender> currentHost = new AtomicReference<>();
    private int endWave = 0;
    private boolean enabled = false;
    private boolean useLockedKit = false;
    private boolean freeForAll = false;
    private boolean stopOnGameEnd = false;
    private boolean allowSpectator = false;
    private boolean allowRespawn = true;

    public TournamentArena(String arena, List<KitCondition> kitConditions) {
        this.arena = arena;
        this.kitConditions = kitConditions;
    }

    public String getArena() {
        return arena;
    }

    public Optional<Kit> parseDefaultKit() {
        if (kitConditions.isEmpty()) {
            return Optional.empty();
        }
        List<Kit> kits = getKits();
        if (kits.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(kits.get(0));
    }

    public List<Kit> getKits() {
        return KitRegistry.getKits().parallelStream().filter(this::isKitAllowed).collect(Collectors.toList());
    }

    public boolean isKitAllowed(Kit kit) {
        if (kitConditions.isEmpty()) {
            return true;
        }
        boolean allowed = false;
        for (KitCondition kitCondition : kitConditions) {
            if (kitCondition.isKitAllowed(kit)) {
                allowed = true;
                break;
            }
        }
        return allowed;
    }

    public List<UUID> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public void addPlayer(UUID uuid) {
        players.add(uuid);
    }

    public void addPlayer(OfflinePlayer player) {
        addPlayer(player.getUniqueId());
    }

    public void addSpectator(UUID uuid) {
        spectators.add(uuid);
    }

    public void addSpectator(OfflinePlayer player) {
        addSpectator(player.getUniqueId());
    }

    public void clearAllUuids() {
        players.clear();
        spectators.clear();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!this.enabled) {
            clearAllUuids();
        }
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

    public boolean isAllowRespawn() {
        return allowRespawn;
    }

    public void setAllowRespawn(boolean allowRespawn) {
        this.allowRespawn = allowRespawn;
    }

    public int getEndWave() {
        return endWave;
    }

    public void setEndWave(int endWave) {
        this.endWave = endWave;
    }
}
