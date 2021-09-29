package me.hsgamer.villagedefensetournaments.manager;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.villagedefensetournaments.VillageDefenseTournaments;
import me.hsgamer.villagedefensetournaments.arena.TournamentArena;
import me.hsgamer.villagedefensetournaments.builder.KitConditionBuilder;
import me.hsgamer.villagedefensetournaments.config.ArenaConfig;
import me.hsgamer.villagedefensetournaments.kitcondition.KitCondition;

import java.util.*;

public class TournamentArenaManager {
    private final Map<String, TournamentArena> map = new HashMap<>();
    private final VillageDefenseTournaments instance;

    public TournamentArenaManager(VillageDefenseTournaments instance) {
        this.instance = instance;
    }

    public void loadTournamentArenas() {
        ArenaConfig arenaConfig = instance.getArenaConfig();
        for (String key : arenaConfig.getKeys(false)) {
            Map<String, Object> value = arenaConfig.getNormalizedValues(key, false);
            if (!value.containsKey("arena")) {
                continue;
            }
            String arena = String.valueOf(value.get("arena"));
            List<KitCondition> kitConditions = Optional.ofNullable(value.get("kits"))
                    .map(o -> CollectionUtils.createStringListFromObject(o, true))
                    .map(KitConditionBuilder.INSTANCE::getKitConditions)
                    .orElse(Collections.emptyList());

            TournamentArena tournamentArena = new TournamentArena(arena, kitConditions);
            Optional.ofNullable(value.get("use-locked-kit"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean)
                    .ifPresent(tournamentArena::setUseLockedKit);
            Optional.ofNullable(value.get("free-for-all"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean)
                    .ifPresent(tournamentArena::setFreeForAll);
            Optional.ofNullable(value.get("stop-on-game-end"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean)
                    .ifPresent(tournamentArena::setStopOnGameEnd);
            Optional.ofNullable(value.get("allow-spectator"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean)
                    .ifPresent(tournamentArena::setAllowSpectator);
            Optional.ofNullable(value.get("allow-respawn"))
                    .map(String::valueOf)
                    .map(Boolean::parseBoolean)
                    .ifPresent(tournamentArena::setAllowRespawn);
            Optional.ofNullable(value.get("end-wave"))
                    .map(String::valueOf)
                    .map(Integer::parseInt)
                    .ifPresent(tournamentArena::setEndWave);

            addTournamentArena(key, tournamentArena);
        }
    }

    public void addTournamentArena(String name, TournamentArena tournamentArena) {
        map.put(name, tournamentArena);
    }

    public Optional<TournamentArena> getTournamentArena(String name) {
        return Optional.ofNullable(map.get(name));
    }

    public void clearAll() {
        map.values().forEach(tournamentArena -> tournamentArena.setEnabled(false));
        map.clear();
    }

    public Collection<String> getAllNames() {
        return map.keySet();
    }

    public Optional<TournamentArena> getTournamentArenaFromGameArena(String arenaId) {
        return map.values().parallelStream().filter(arena -> arena.getArena().equals(arenaId)).findFirst();
    }

    public Optional<TournamentArena> getEnabledTournamentArenaFromGameArena(String arenaId) {
        return getTournamentArenaFromGameArena(arenaId).filter(TournamentArena::isEnabled);
    }

    public boolean checkTournamentArenaEnabled(String arenaId) {
        return map.values().parallelStream()
                .filter(arena -> arena.getArena().equals(arenaId))
                .anyMatch(TournamentArena::isEnabled);
    }
}
