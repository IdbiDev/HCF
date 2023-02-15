package me.idbi.hcf.commands.allyCommands;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.AllyTools;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;


public class ally_Accept {
    //factionname = teszt
    public static void AcceptAlly(Player p, String factionname) {

        Faction factionTarget = playertools.getFactionByName(factionname);
        Faction factionPlayer = playertools.getPlayerFaction(p);
        if (factionPlayer != null) {
            if (factionTarget != null) {
                if(playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_ALL)) {
                    if (factionTarget.isFactionAllyInvited(factionPlayer)) {
                        AllyTools.addAlly(factionPlayer, factionTarget);
                        for(Player member : playertools.getFactionOnlineMembers(factionPlayer)){
                            member.sendMessage(Messages.joined_ally.language(member).setFaction(factionTarget).queue());
                        }
                        for(Player member : playertools.getFactionOnlineMembers(factionTarget)){
                            member.sendMessage(Messages.joined_ally.language(member).setFaction(factionPlayer).queue());
                        }
                    } else {
                        p.sendMessage(Messages.not_invited_ally.language(p).queue());
                    }
                }else{
                    p.sendMessage(Messages.no_permission.language(p).queue());
                }
            } else {
                //Nem vagy meghíva ebbe a facionbe
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        } else {
            // Már vagy egy factionbe
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
