package me.hsgamer.villagedefensetournaments.command;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.villagedefensetournaments.Permissions;
import me.hsgamer.villagedefensetournaments.VillageDefenseTournaments;
import me.hsgamer.villagedefensetournaments.arena.TournamentArena;
import me.hsgamer.villagedefensetournaments.config.MessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import plugily.projects.villagedefense.arena.Arena;
import plugily.projects.villagedefense.arena.ArenaManager;
import plugily.projects.villagedefense.arena.ArenaRegistry;
import plugily.projects.villagedefense.arena.ArenaState;

import java.util.*;

public class HostCommand extends Command {
    private static final String ADD_PLAYER = "addplayer";
    private static final String CLEAR_PLAYERS = "clearplayers";
    private static final String START = "start";
    private static final String END = "end";

    private final VillageDefenseTournaments instance;

    public HostCommand(VillageDefenseTournaments instance) {
        super("tournamenthost", "Tournament host command", "/tournamenthost", Collections.singletonList("host"));
        this.instance = instance;

        setPermission(Permissions.HOST.getName());
    }

    @Override
    @SuppressWarnings("deprecated")
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        if (args.length < 2) {
            MessageUtils.sendMessage(sender, "&b/" + commandLabel + " addplayer <arena> <player>");
            MessageUtils.sendMessage(sender, "&b/" + commandLabel + " clearplayers <arena>");
            MessageUtils.sendMessage(sender, "&b/" + commandLabel + " start <arena>");
            MessageUtils.sendMessage(sender, "&b/" + commandLabel + " end <arena>");
            return true;
        }

        Optional<TournamentArena> optional = instance.getTournamentArenaManager().getTournamentArena(args[1]);
        if (!optional.isPresent()) {
            MessageUtils.sendMessage(sender, MessageConfig.CANNOT_FIND_TOURNAMENT_ARENA.getValue());
            return false;
        }

        TournamentArena tournamentArena = optional.get();
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case ADD_PLAYER: {
                if (args.length < 3) {
                    MessageUtils.sendMessage(sender, "&b/" + commandLabel + " addplayer <arena> <player>");
                    break;
                }
                // noinspection deprecation
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                tournamentArena.addPlayer(offlinePlayer);
                MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
                break;
            }
            case CLEAR_PLAYERS: {
                tournamentArena.clearAllUuids();
                MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
                break;
            }
            case START: {
                if (tournamentArena.isEnabled()) {
                    MessageUtils.sendMessage(sender, MessageConfig.TOURNAMENT_ARENA_ALREADY_STARTED.getValue());
                    break;
                }
                if (instance.getTournamentArenaManager().checkTournamentArenaEnabled(tournamentArena.getArena())) {
                    MessageUtils.sendMessage(sender, MessageConfig.ARENA_ON_TOURNAMENT.getValue());
                    break;
                }
                Arena arena = ArenaRegistry.getArena(tournamentArena.getArena());
                if (arena != null) {
                    if (arena.getArenaState() == ArenaState.IN_GAME) {
                        ArenaManager.stopGame(false, arena);
                        arena.getPlayers().forEach(player -> MessageUtils.sendMessage(player, MessageConfig.TOURNAMENT_ARENA_INGAME_BROADCAST.getValue()));
                    } else if (arena.getArenaState() == ArenaState.STARTING || arena.getArenaState() == ArenaState.WAITING_FOR_PLAYERS) {
                        arena.getPlayers().forEach(player -> MessageUtils.sendMessage(player, MessageConfig.TOURNAMENT_ARENA_INGAME_BROADCAST.getValue()));
                        arena.getPlayers().forEach(player -> ArenaManager.leaveAttempt(player, arena));
                    }
                }
                tournamentArena.setEnabled(true);
                MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
                break;
            }
            case END: {
                if (!tournamentArena.isEnabled()) {
                    MessageUtils.sendMessage(sender, MessageConfig.TOURNAMENT_ARENA_ALREADY_ENDED.getValue());
                    break;
                }
                tournamentArena.setEnabled(false);
                MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
                break;
            }
            default: {
                MessageUtils.sendMessage(sender, "&b/" + commandLabel + " addplayer <arena> <player>");
                MessageUtils.sendMessage(sender, "&b/" + commandLabel + " clearplayers <arena>");
                MessageUtils.sendMessage(sender, "&b/" + commandLabel + " start <arena>");
                MessageUtils.sendMessage(sender, "&b/" + commandLabel + " end <arena>");
                break;
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (!testPermissionSilent(sender)) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return Arrays.asList(ADD_PLAYER, CLEAR_PLAYERS, START, END);
        }
        if (args.length == 2 && Arrays.asList(ADD_PLAYER, CLEAR_PLAYERS, START, END).contains(args[0])) {
            return new ArrayList<>(instance.getTournamentArenaManager().getAllNames());
        }
        if (args.length == 3 && ADD_PLAYER.equalsIgnoreCase(args[0])) {
            return super.tabComplete(sender, alias, args);
        }
        return Collections.emptyList();
    }
}