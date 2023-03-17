package me.idbi.hcf.Tools.Objects;

import lombok.Getter;
import me.idbi.hcf.Main;

import java.sql.Connection;
import java.util.HashMap;


public class AllyFaction {
    private final Connection con = AllyCon.con;
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

class AllyCon {
    protected static final Connection con = Main.getConnection();

}
/*
    CukiMC:
        KisCat ALLY
        -Jogok CukiMC területén:
            -asd
            -asd
            -Kurva

    KisCat:
        CukiMC ALLY
        -Jogok kiscica területén:
            -ASKED
   Értesítés minden esetbe ha változás van
 */


/*
    Faction  -> Allies 1. ->permissions
 */

