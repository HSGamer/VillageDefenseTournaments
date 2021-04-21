package me.hsgamer.villagedefensetournaments.command;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.villagedefensetournaments.Permissions;
import me.hsgamer.villagedefensetournaments.VillageDefenseTournaments;
import me.hsgamer.villagedefensetournaments.config.MessageConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class ReloadCommand extends Command {
    private final VillageDefenseTournaments instance;

    public ReloadCommand(VillageDefenseTournaments instance) {
        super("reloadtournaments", "Reload the plugin", "/reloadtournaments", Collections.singletonList("rltournaments"));
        this.instance = instance;

        setPermission(Permissions.RELOAD.getName());
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!testPermission(sender)) {
            return false;
        }

        instance.getTournamentArenaManager().clearAll();
        instance.getArenaConfig().reload();
        instance.getMessageConfig().reload();
        instance.getTournamentArenaManager().loadTournamentArenas();
        MessageUtils.sendMessage(sender, MessageConfig.SUCCESS.getValue());
        return true;
    }
}
