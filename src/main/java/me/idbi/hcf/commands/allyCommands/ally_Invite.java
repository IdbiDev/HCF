package me.idbi.hcf.commands.allyCommands;

import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.factionhistorys.HistoryEntrys;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class ally_Invite {
    public static void InvitePlayerToFaction(Player p, String name) {
        if (!playertools.getMetadata(p, "factionid").equalsIgnoreCase("0")) {
            if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_ALL)) {
                //Todo nincs jog
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            Faction target = Main.nameToFaction.get(name);
            if (target != null) {
                if (playertools.isFactionOnline(target)) {
                    Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));

                    if (!faction.isFactionAllyInvited(target)) {
                        faction.inviteFactionAlly(target);
                        // Broadcast both player the invite successfully

                       // p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());

                        /*Clickable_Join.sendMessage(target,
                                "/f join " + faction.name,
                                Messages.invited_by.language(p).setPlayer(p).queue(),
                                Messages.hover_join.language(p).queue());


                        for (Player member : faction.getMembers()) {
                            member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                        }*/

                    } else {
                        // This player is already invited
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
