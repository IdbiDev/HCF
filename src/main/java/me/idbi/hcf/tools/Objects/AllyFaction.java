package me.idbi.hcf.tools.Objects;

import me.idbi.hcf.Main;
import me.idbi.hcf.tools.SQL_Connection;
import org.json.JSONObject;

import java.sql.Connection;
import java.util.HashMap;



public class AllyFaction {
    private final Connection con = AllyTools.con;
    private final Faction AllyFaction;
    private final int thisid;
    private final HashMap<Permissions, Boolean> class_permissions = new HashMap<>();
    public AllyFaction(int thisid,Faction AllyFaction) {
        this.AllyFaction = AllyFaction;
        this.thisid = thisid;
        for(Permissions p : Permissions.values()){
            class_permissions.put(p,false);
        }
        loadPermissions();
    }

    public Faction getAllyFaction(){
        return this.AllyFaction;
    }
    private void loadPermissions() {
        try {
            HashMap<String, Object> permissionmap = SQL_Connection.dbPoll(con, "SELECT * FROM allyfactions WHERE ID='?'", String.valueOf(thisid));
            JSONObject perms = new JSONObject((String) permissionmap.get("permissions"));
            for(Permissions name : Permissions.values()) {
                class_permissions.put(name, (Boolean) perms.get(name.name()));
            }
        } catch (Exception uwu) {
            uwu.printStackTrace();
        }
    }
    public boolean hasPermission(Permissions perm) {
        return class_permissions.get(perm);
    }
    public void setPermission(Permissions perm, boolean state) {
        class_permissions.put(perm,state);
        saveRank();
    }
    public void saveRank(){
        SQL_Connection.dbExecute(con,"UPDATE allyfactions SET permissions = '?' WHERE ID = '?'",
                new JSONObject(class_permissions).toString(),
                String.valueOf(thisid)
        );
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


