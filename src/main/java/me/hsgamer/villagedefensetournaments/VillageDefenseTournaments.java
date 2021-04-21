package me.hsgamer.villagedefensetournaments;

import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.villagedefensetournaments.command.HostCommand;
import me.hsgamer.villagedefensetournaments.command.ReloadCommand;
import me.hsgamer.villagedefensetournaments.config.ArenaConfig;
import me.hsgamer.villagedefensetournaments.config.MessageConfig;
import me.hsgamer.villagedefensetournaments.listener.GameListener;
import me.hsgamer.villagedefensetournaments.manager.TournamentArenaManager;
import org.bukkit.plugin.java.JavaPlugin;
import plugily.projects.villagedefense.Main;

public final class VillageDefenseTournaments extends BasePlugin {
    private final MessageConfig messageConfig = new MessageConfig(this);
    private final ArenaConfig arenaConfig = new ArenaConfig(this);

    private final TournamentArenaManager tournamentArenaManager = new TournamentArenaManager(this);
    private Main parentPlugin;

    @Override
    public void load() {
        MessageUtils.setPrefix(MessageConfig.PREFIX::getValue);
        messageConfig.setup();
        arenaConfig.setup();
    }

    @Override
    public void enable() {
        parentPlugin = JavaPlugin.getPlugin(Main.class);
        registerListener(new GameListener(this));

        registerCommand(new ReloadCommand(this));
        registerCommand(new HostCommand(this));
    }

    @Override
    public void postEnable() {
        tournamentArenaManager.loadTournamentArenas();
    }

    @Override
    public void disable() {
        tournamentArenaManager.clearAll();
    }

    public TournamentArenaManager getTournamentArenaManager() {
        return tournamentArenaManager;
    }

    public Main getParentPlugin() {
        return parentPlugin;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public ArenaConfig getArenaConfig() {
        return arenaConfig;
    }
}
