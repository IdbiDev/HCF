package me.idbi.hcf.Tools;

import me.idbi.hcf.Tools.Objects.Faction;

import java.util.ArrayList;

public class AllyManager {
    private final ArrayList<Faction> invitedAllies = new ArrayList<>();

    public void inviteFactionToAlly(Faction faction) {
        if (!invitedAllies.contains(faction)) {
            invitedAllies.add(faction);
        }
    }

    public boolean removePlayerFromInvite(Faction faction) {
        if (invitedAllies.contains(faction)) {
            invitedAllies.remove(faction);
            return true;
        }
        return false;
    }

    public boolean isFactionInvited(Faction faction) {
        return invitedAllies.contains(faction);
    }

    public ArrayList<Faction> getInvitedAllies() {
        return invitedAllies;
    }
}

