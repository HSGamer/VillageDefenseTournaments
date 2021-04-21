package me.hsgamer.villagedefensetournaments;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

public class Permissions {
    public static final Permission HOST = new Permission("vdtour.host", PermissionDefault.OP);
    public static final Permission RELOAD = new Permission("vdtour.reload", PermissionDefault.OP);

    private Permissions() {
        // EMPTY
    }
}
