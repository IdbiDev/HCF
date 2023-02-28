package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.Main;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AllyTools {

    public static String getAlliesJson(Faction mainFaction) {
        HashMap<Integer, AllyFaction> allies = mainFaction.Allies;

        // {85:{"FRIENDLY_FIRE":false,"USEBLOCK":false,"VIEWITEMS":false,"BREAKBLOCK":false},AllyID:}

        JSONObject mainJson = new JSONObject();
        for (Map.Entry<Integer, AllyFaction> allyHash : allies.entrySet()) {
            AllyFaction ally = allyHash.getValue();
            JSONObject permissions = new JSONObject();
            for (Map.Entry<Permissions, Boolean> perms : ally.getPermissions().entrySet()) {
                permissions.put(perms.getKey().name(), perms.getValue());
            }

            mainJson.put(String.valueOf(ally.getAllyFaction().id), permissions);
        }
        System.out.println(mainJson);
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
        AllyFaction mainFactionAlly = new AllyFaction(allyFaction.id, allyFaction);
        mainFaction.Allies.put(mainFactionAlly.getFactionId(), mainFactionAlly);
        Main.sendCmdMessage("Ally added! Requester: " + mainFaction.name + " Receiver: " + allyFaction.name);
        mainFaction.allyinvites.removePlayerFromInvite(allyFaction);

        //For the allay faction!
        mainFactionAlly = new AllyFaction(mainFaction.id, mainFaction);
        allyFaction.Allies.put(mainFactionAlly.getFactionId(), mainFactionAlly);
        Main.sendCmdMessage("Ally added! Requester: " + allyFaction.name + " Receiver: " + mainFaction.name);
        allyFaction.allyinvites.removePlayerFromInvite(mainFaction);

    }
}
