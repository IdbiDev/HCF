package me.idbi.hcf.tools;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class inviteManager {

    public static class factionInvite {
        private Integer faction;
        public factionInvite(Integer id){
            this.faction = id;
        }
        private final ArrayList<Player> invitedPlayers = new ArrayList();

        public void invitePlayerToFaction(Player p){
            if (!invitedPlayers.contains(p)){
                invitedPlayers.add(p);
            }
        }
        public boolean removePlayerFromInvite(Player p){
            if(invitedPlayers.contains(p)){
                invitedPlayers.remove(p);
                return true;
            }
            return false;
        }
        public boolean isPlayerInvited(Player p){
            return invitedPlayers.contains(p);
        }

        public Integer getFactionID(){
            return faction;
        }
    }
}
