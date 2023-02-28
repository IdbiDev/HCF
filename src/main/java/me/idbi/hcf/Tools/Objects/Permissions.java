package me.idbi.hcf.Tools.Objects;


public enum Permissions {
    FRIENDLY_FIRE,
    INTERACT,
    PLACE_BLOCK,
    BREAK_BLOCK,
    INVENTORY_ACCESS;

    public static Permissions getByName(String name) {
        for (Permissions perms : Permissions.values()) {
            if (perms.name().equalsIgnoreCase(name))
                return perms;
        }
        return null;
    }
}
