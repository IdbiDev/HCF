package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Database.MongoDB.MongoDBDriver;
import me.idbi.hcf.Tools.Database.MongoDB.MongoFields;
import me.idbi.hcf.Tools.Database.MySQL.SQL_Connection;
import org.bson.conversions.Bson;
import org.json.JSONObject;

import java.util.Map;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static me.idbi.hcf.Tools.Playertools.con;

public class AllyTools {

    public static String getAlliesJson(Faction mainFaction) {
        // {85:{"FRIENDLY_FIRE":false,"USEBLOCK":false,"VIEWITEMS":false,"BREAKBLOCK":false},AllyID:}

        JSONObject mainJson = new JSONObject();
        for (Map.Entry<Integer, AllyFaction> allyHash : mainFaction.getAllies().entrySet()) {
            AllyFaction ally = allyHash.getValue();
            JSONObject permissions = new JSONObject();
            for (Map.Entry<Permissions, Boolean> perms : ally.getPermissions().entrySet()) {
                permissions.put(perms.getKey().name(), perms.getValue());
            }

            mainJson.put(String.valueOf(ally.getAllyFaction().getId()), permissions);
        }
        return mainJson.toString();
    }


    /**
     * This function will add each ally element.
     *
     * @param mainFaction mainFaction is the "Leader"
     * @param allyFaction allyFaction is the sub "Leader"
     */
    public static void addAlly(Faction mainFaction, Faction allyFaction) {
        //For this faction!
        AllyFaction mainFactionAlly = new AllyFaction(allyFaction.getId(), allyFaction);
        mainFaction.addAlly(mainFactionAlly);
        mainFaction.getAllyInvites().removePlayerFromInvite(allyFaction);
        String AlliesEntry = AllyTools.getAlliesJson(mainFaction);
        if(Main.isUsingMongoDB()){
            Bson where = eq(MongoFields.FactionsFields.ID.get(),mainFaction.getId());
            Bson updates = set(MongoFields.FactionsFields.ALLIES.get(), AlliesEntry);

            MongoDBDriver.Update(MongoDBDriver.MongoCollections.FACTIONS,where,updates);
        }else {
            SQL_Connection.dbExecute(con,"UPDATE factions SET Allies = '?' WHERE ID = '?'",AlliesEntry, String.valueOf(mainFaction.getId()));
        }
        //For the ally faction!
        mainFactionAlly = new AllyFaction(mainFaction.getId(), mainFaction);
        allyFaction.addAlly(mainFactionAlly);
        allyFaction.getAllyInvites().removePlayerFromInvite(mainFaction);
        AlliesEntry = AllyTools.getAlliesJson(allyFaction);
        if(Main.isUsingMongoDB()){
            Bson where = eq(MongoFields.FactionsFields.ID.get(),allyFaction.getId());
            Bson updates = set(MongoFields.FactionsFields.ALLIES.get(), AlliesEntry);

            MongoDBDriver.Update(MongoDBDriver.MongoCollections.FACTIONS,where,updates);
        }else {
            SQL_Connection.dbExecute(con,"UPDATE factions SET Allies = '?' WHERE ID = '?'",AlliesEntry, String.valueOf(allyFaction.getId()));
        }


    }
}
