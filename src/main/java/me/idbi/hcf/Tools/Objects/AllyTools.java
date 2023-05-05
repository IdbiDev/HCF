package me.idbi.hcf.Tools.Objects;

import org.json.JSONObject;

import java.util.Map;

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

        //For the ally faction!
        mainFactionAlly = new AllyFaction(mainFaction.getId(), mainFaction);
        allyFaction.addAlly(mainFactionAlly);
        allyFaction.getAllyInvites().removePlayerFromInvite(mainFaction);

    }
}
