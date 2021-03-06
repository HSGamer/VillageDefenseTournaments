package me.hsgamer.villagedefensetournaments.listener;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.villagedefensetournaments.VillageDefenseTournaments;
import me.hsgamer.villagedefensetournaments.arena.TournamentArena;
import me.hsgamer.villagedefensetournaments.config.MessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import plugily.projects.villagedefense.api.event.game.VillageGameJoinAttemptEvent;
import plugily.projects.villagedefense.api.event.game.VillageGameStartEvent;
import plugily.projects.villagedefense.api.event.game.VillageGameStopEvent;
import plugily.projects.villagedefense.api.event.player.VillagePlayerChooseKitEvent;
import plugily.projects.villagedefense.api.event.player.VillagePlayerRespawnEvent;
import plugily.projects.villagedefense.api.event.wave.VillageWaveEndEvent;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaManager;
import plugily.projects.villagedefense.arena.ArenaState;
import plugily.projects.villagedefense.handlers.language.Messages;
import plugily.projects.villagedefense.kits.basekits.Kit;
import plugily.projects.villagedefense.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameListener implements Listener {
    private final VillageDefenseTournaments instance;

    public GameListener(VillageDefenseTournaments instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onGameJoin(VillageGameJoinAttemptEvent event) {
        User user = instance.getParentPlugin().getUserManager().getUser(event.getPlayer());
        Arena arena = event.getArena();

        Optional<TournamentArena> optionalTournamentArena = instance.getTournamentArenaManager().getEnabledTournamentArenaFromGameArena(arena.getId());
        if (!optionalTournamentArena.isPresent()) {
            return;
        }
        TournamentArena tournamentArena = optionalTournamentArena.get();

        if (!tournamentArena.isFreeForAll()) {
            if (
                    tournamentArena.isAllowSpectator()
                            || user.getPlayer().equals(tournamentArena.getCurrentHost())
                            || tournamentArena.getSpectators().contains(user.getUniqueId())
            ) {
                user.isPermanentSpectator();
            } else if (!tournamentArena.getPlayers().contains(user.getUniqueId())) {
                MessageUtils.sendMessage(user.getPlayer(), MessageConfig.CANNOT_JOIN_TOURNAMENT_ARENA.getValue());
                event.setCancelled(true);
                return;
            }
        }
        MessageUtils.sendMessage(user.getPlayer(), MessageConfig.TOURNAMENT_ARENA_JOINED.getValue());
        List<Kit> kits = tournamentArena.getKits();
        if (!kits.isEmpty()) {
            MessageUtils.sendMessage(user.getPlayer(), MessageConfig.TOURNAMENT_ARENA_ALLOWED_KITS.getValue());
            kits.forEach(kit -> MessageUtils.sendMessage(user.getPlayer(), "&7- &f" + kit.getName()));

            if (arena.getArenaState() != ArenaState.IN_GAME || !tournamentArena.isKitAllowed(user.getKit())) {
                tournamentArena.parseDefaultKit().ifPresent(user::setKit);
            }
        }
    }

    @EventHandler
    public void onGameStart(VillageGameStartEvent event) {
        Arena arena = event.getArena();

        Optional<TournamentArena> optionalTournamentArena = instance.getTournamentArenaManager().getEnabledTournamentArenaFromGameArena(arena.getId());
        if (!optionalTournamentArena.isPresent()) {
            return;
        }
        TournamentArena tournamentArena = optionalTournamentArena.get();

        Optional<Kit> optional = tournamentArena.parseDefaultKit();
        if (!optional.isPresent()) {
            return;
        }
        List<Player> spectators = new ArrayList<>();
        for (Player player : arena.getPlayers()) {
            if (tournamentArena.isFreeForAll() || tournamentArena.getPlayers().contains(player.getUniqueId())) {
                User user = instance.getParentPlugin().getUserManager().getUser(player);
                if (!tournamentArena.isKitAllowed(user.getKit())) {
                    user.setKit(optional.get());
                }
            } else {
                spectators.add(player);
            }
        }

        // Schedule to kill spectators as there is no event for spectating on game start
        if (!spectators.isEmpty()) {
            Bukkit.getScheduler().runTaskLater(instance, () -> spectators.forEach(spectator -> spectator.setHealth(0)), 10);
        }
    }

    @EventHandler
    public void onRespawn(VillagePlayerRespawnEvent event) {
        Arena arena = event.getArena();
        Player player = event.getPlayer();

        Optional<TournamentArena> optionalTournamentArena = instance.getTournamentArenaManager().getEnabledTournamentArenaFromGameArena(arena.getId());
        if (!optionalTournamentArena.isPresent()) {
            return;
        }
        TournamentArena tournamentArena = optionalTournamentArena.get();

        if (
                (!tournamentArena.isFreeForAll() && !tournamentArena.getPlayers().contains(player.getUniqueId()))
                        || !tournamentArena.isAllowRespawn()
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKitChoose(VillagePlayerChooseKitEvent event) {
        User user = instance.getParentPlugin().getUserManager().getUser(event.getPlayer());
        Arena arena = event.getArena();
        Kit kit = event.getKit();

        Optional<TournamentArena> optionalTournamentArena = instance.getTournamentArenaManager().getEnabledTournamentArenaFromGameArena(arena.getId());
        if (!optionalTournamentArena.isPresent()) {
            return;
        }
        TournamentArena tournamentArena = optionalTournamentArena.get();

        if (!tournamentArena.isKitAllowed(kit)) {
            MessageUtils.sendMessage(user.getPlayer(), MessageConfig.CANNOT_USE_THIS_KIT.getValue());
            event.setCancelled(true);
            return;
        }

        if (tournamentArena.isUseLockedKit() && !kit.isUnlockedByPlayer(user.getPlayer())) {
            event.setCancelled(true);
            user.setKit(kit);
            user.getPlayer().sendMessage(Messages.KITS_CHOOSE_MESSAGE.getMessage().replace("%KIT%", kit.getName()));
        }
    }

    @EventHandler
    public void onGameEnd(VillageGameStopEvent event) {
        instance.getTournamentArenaManager()
                .getEnabledTournamentArenaFromGameArena(event.getArena().getId())
                .filter(TournamentArena::isStopOnGameEnd)
                .ifPresent(tournamentArena -> tournamentArena.setEnabled(false));
    }

    @EventHandler
    public void onEndWave(VillageWaveEndEvent event) {
        Arena arena = event.getArena();
        Optional<TournamentArena> optionalTournamentArena = instance.getTournamentArenaManager().getEnabledTournamentArenaFromGameArena(arena.getId());
        if (!optionalTournamentArena.isPresent()) {
            return;
        }
        TournamentArena tournamentArena = optionalTournamentArena.get();

        int endWave = tournamentArena.getEndWave();
        if (endWave <= 0) {
            return;
        }

        if (arena.getWave() >= endWave) {
            Bukkit.getScheduler().runTask(instance, () -> ArenaManager.stopGame(false, arena));
        }
    }
}
