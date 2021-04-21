package me.hsgamer.villagedefensetournaments.config;

import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.PathableConfig;
import me.hsgamer.hscore.config.path.StringConfigPath;
import org.bukkit.plugin.Plugin;

public class MessageConfig extends PathableConfig {
    public static final StringConfigPath PREFIX = new StringConfigPath("prefix", "&7[&aTournaments&7] ");
    public static final StringConfigPath SUCCESS = new StringConfigPath("success", "&aSuccess");
    public static final StringConfigPath CANNOT_JOIN_TOURNAMENT_ARENA = new StringConfigPath("cannot-join-tournament-arena", "&cYou are not in the allowed list to join this arena");
    public static final StringConfigPath CANNOT_USE_THIS_KIT = new StringConfigPath("cannot-use-this-kit", "&cYou can not use this kit in the arena");

    public static final StringConfigPath CANNOT_FIND_TOURNAMENT_ARENA = new StringConfigPath("cannot-find-tournament-arena", "&cThis arena doesn't exist");
    public static final StringConfigPath TOURNAMENT_ARENA_ALREADY_STARTED = new StringConfigPath("tournament-arena-already-started", "&cThis arena is already started");
    public static final StringConfigPath TOURNAMENT_ARENA_ALREADY_ENDED = new StringConfigPath("tournament-arena-already-ended", "&cThis arena is already ended");
    public static final StringConfigPath ARENA_ON_TOURNAMENT = new StringConfigPath("arena-on-tournament", "&cAnother tournament is held on the game arena");

    public static final StringConfigPath TOURNAMENT_ARENA_INGAME_BROADCAST = new StringConfigPath("tournament-arena-ingame-broadcase", "&bThis arena is chosen to be a tournament arena");
    public static final StringConfigPath TOURNAMENT_ARENA_JOINED = new StringConfigPath("tournament-arena-join", "&bYou are playing a tournament arena");
    public static final StringConfigPath TOURNAMENT_ARENA_ALLOWED_KITS = new StringConfigPath("tournament-arena-allowed-kits", "&6Allowed Kits:");

    public MessageConfig(Plugin plugin) {
        super(new BukkitConfig(plugin, "messages.yml"));
    }
}
