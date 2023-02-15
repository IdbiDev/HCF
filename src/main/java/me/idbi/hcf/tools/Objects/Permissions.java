package me.idbi.hcf.tools.Objects;


public enum Permissions {
        FRIENDLY_FIRE,
        USEBLOCK,
        BREAKBLOCK,
        VIEWITEMS;

        public static Permissions getByName(String name) {
                for (Permissions perms : Permissions.values()) {
                        if (perms.name().equalsIgnoreCase(name))
                                return perms;
                }
                return null;
        }
}
