package me.hsgamer.villagedefensetournaments.manager;

import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.villagedefensetournaments.VillageDefenseTournaments;
import me.hsgamer.villagedefensetournaments.arena.TournamentArena;
import me.hsgamer.villagedefensetournaments.config.ArenaConfig;

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
            List<String> kits = Optional.ofNullable(value.get("kits")).map(o -> CollectionUtils.createStringListFromObject(o, true)).orElse(Collections.emptyList());

            TournamentArena tournamentArena = new TournamentArena(arena, kits);
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
        map.clear();
    }

    public Collection<String> getAllNames() {
        return map.keySet();
    }

    public Optional<TournamentArena> getTournamentArenaFromGameArena(String arenaId) {
        return map.values().parallelStream().filter(arena -> arena.getArena().equals(arenaId)).findFirst();
    }

    public boolean checkTournamentArenaEnabled(String arenaId) {
        return map.values().parallelStream()
                .filter(arena -> arena.getArena().equals(arenaId))
                .anyMatch(TournamentArena::isEnabled);
    }
}
