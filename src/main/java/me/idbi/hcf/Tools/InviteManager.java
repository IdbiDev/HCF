package me.idbi.hcf.Tools;

import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class InviteManager {

    public static class FactionInvite {
        private final ArrayList<HCFPlayer> invitedPlayers = new ArrayList<>();

        public void invitePlayerToFaction(Player p) {
            HCFPlayer player = HCFPlayer.getPlayer(p);
            if (!invitedPlayers.contains(player)) {
                invitedPlayers.add(player);
            }
        }

        public boolean removePlayerFromInvite(Player p) {
            HCFPlayer player = HCFPlayer.getPlayer(p);
            if (invitedPlayers.contains(player)) {
                invitedPlayers.remove(player);
                return true;
            }
            return false;
        }
        public boolean removePlayerFromInvite(HCFPlayer player) {
            if (invitedPlayers.contains(player)) {
                invitedPlayers.remove(player);
                return true;
            }
            return false;
        }

        public boolean isPlayerInvited(Player p) {
            HCFPlayer player = HCFPlayer.getPlayer(p);
            return invitedPlayers.contains(player);
        }

        public boolean isPlayerInvited(HCFPlayer p) {
            return invitedPlayers.contains(p);
        }

        public ArrayList<HCFPlayer> getInvitedPlayers() {
            return invitedPlayers;
        }
    }
}
