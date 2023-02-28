package me.idbi.hcf.Tools;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InviteManager {

    public static class factionInvite {
        private final ArrayList<Player> invitedPlayers = new ArrayList<>();

        public void invitePlayerToFaction(Player p) {
            if (!invitedPlayers.contains(p)) {
                invitedPlayers.add(p);
            }
        }

        public boolean removePlayerFromInvite(Player p) {
            if (invitedPlayers.contains(p)) {
                invitedPlayers.remove(p);
                return true;
            }
            return false;
        }

        public boolean isPlayerInvited(Player p) {
            return invitedPlayers.contains(p);
        }

        public ArrayList<Player> getInvitedPlayers() {
            return invitedPlayers;
        }
    }
}
