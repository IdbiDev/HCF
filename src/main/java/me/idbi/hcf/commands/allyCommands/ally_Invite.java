package me.idbi.hcf.commands.allyCommands;

import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class ally_Invite {
    //name = teszt
    public static void InvitePlayerToFaction(Player p, String name) {
        Faction target = playertools.getFactionByName(name);
        Faction playerFaction = playertools.getPlayerFaction(p);
        if (playerFaction != null) {
            if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_ALL)) {
                //Todo nincs jog
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            if (target != null) {
                if (playertools.isFactionOnline(target)) {
                    //Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));

                    if (!playerFaction.isFactionAllyInvited(target) && !playerFaction.isAlly(target)) {
                        playerFaction.inviteFactionAlly(target);
                        // Broadcast both player the invite successfully

                       // p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());

                        /*Clickable_Join.sendMessage(target,
                                "/f join " + faction.name,
                                Messages.invited_by.language(p).setPlayer(p).queue(),
                                Messages.hover_join.language(p).queue());


                        for (Player member : faction.getMembers()) {
                            member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                        }*/
                        for(Player member : playertools.getFactionOnlineMembers(target)){

                            Clickable_Join.sendMessage(member,
                                    "/ally accept " + playerFaction.name,
                                    Messages.faction_invited_ally.language(member).setFaction(playerFaction).setPlayer(p).queue(),
                                    Messages.hover_accept.language(member).queue());
                        }
                        for(Player member : playertools.getFactionOnlineMembers(playerFaction)){
                            member.sendMessage(Messages.faction_invited_ally.language(member).setFaction(target).queue());
                        }
                    } else {
                        p.sendMessage(Messages.already_invited_ally.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.not_found_faction.queue());
                }
            } else {
                p.sendMessage(Messages.not_found_faction.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
