package me.idbi.hcf.Tools.Objects;

import java.util.HashMap;


public class AllyFaction {
    /**
     * Az a faction, akivel szövetkezett a super class.
     */
    private final Faction allyFaction;
    private final int factionId;
    private final HashMap<Permissions, Boolean> class_permissions = new HashMap<>();

    public AllyFaction(int factionid, Faction AllyFaction) {
        this.factionId = factionid;
        this.allyFaction = AllyFaction;
        for (Permissions p : Permissions.values()) {
            class_permissions.put(p, false);
        }
    }

    /**
     * Gets the current AllyFaction Id
     *
     * @return Integer Faction Id
     */
    public int getFactionId() {
        return this.factionId;
    }

    /**
     * Gets the current AllyFaction Class
     *
     * @return Faction (Faction Ally Object)
     */

    public Faction getAllyFaction() {
        return this.allyFaction;
    }

    public HashMap<Permissions, Boolean> getPermissions() {
        return this.class_permissions;
    }

    public boolean hasPermission(Permissions perm) {
        return class_permissions.get(perm);
    }

    public void setPermission(Permissions perm, boolean state) {
        //System.out.println(perm.name() + class_permissions.get(perm) + " ELŐTTE");
        class_permissions.put(perm, state);
        //System.out.println(perm.name() + class_permissions.get(perm) + " UTÁNA");
    }
}