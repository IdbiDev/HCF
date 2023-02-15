package me.idbi.hcf.tools.Objects;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.SQL_Connection;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;



public class AllyFaction {
    private final Connection con = AllyCon.con;
    /**
     * Az a faction, akivel szövetkezett a super class.
     */
    private final Faction AllyFaction;
    private final int thisid;
    private final HashMap<Permissions, Boolean> class_permissions = new HashMap<>();
    public AllyFaction(int thisid,Faction AllyFaction) {
        this.AllyFaction = AllyFaction;
        this.thisid = thisid;
        for(Permissions p : Permissions.values()){
            class_permissions.put(p,false);
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

    public Faction getAllyFaction(){
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
class AllyTools {
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

