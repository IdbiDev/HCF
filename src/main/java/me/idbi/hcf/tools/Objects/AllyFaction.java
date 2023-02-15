package me.idbi.hcf.tools.Objects;

import me.idbi.hcf.Main;

import java.sql.Connection;
import java.util.HashMap;


public class AllyFaction {
    private final Connection con = AllyCon.con;
    /**
     * Az a faction, akivel szövetkezett a super class.
     */
    private final Faction AllyFaction;
    private final int factionid;
    private final HashMap<Permissions, Boolean> class_permissions = new HashMap<>();

    public AllyFaction(int factionid,Faction AllyFaction) {
        this.factionid = factionid;
        this.AllyFaction = AllyFaction;
        for(Permissions p : Permissions.values()){
            class_permissions.put(p, false);
        }
    }

    /**
     * Gets the current AllyFaction Id
     * @return Integer Faction Id
     */
    public int getFactionId(){
        return this.factionid;
    }
    /**
     * Gets the current AllyFaction Class
     * @return Faction (Faction Ally Object)
     */

    public Faction getAllyFaction() {
        return this.AllyFaction;
    }
    public HashMap<Permissions, Boolean> getPermissions() {
        return this.class_permissions;
    }

    public boolean hasPermission(Permissions perm) {
        return class_permissions.get(perm);
    }
    public void setPermission(Permissions perm, boolean state) {
        class_permissions.put(perm,state);
    }
}

class AllyCon {
    protected static final Connection con = Main.getConnection("AllyFaction");

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

