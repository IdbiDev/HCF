package me.idbi.hcf.commands.cmdFunctions;

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

public class Faction_Invite {
    public static void InvitePlayerToFaction(Player p, String name) {
        if (playertools.getPlayerFaction(p) != null) {
            if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_INVITE)) {
                //Todo nincs jog
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            Player target = Bukkit.getPlayer(name);
            if (target != null) {
                if (playertools.getPlayerFaction(target) == null) {
                    Faction faction = playertools.getPlayerFaction(p);

                    if (!faction.isPlayerInvited(target)) {
                        faction.invitePlayer(target);
                        // Broadcast both player the invite successfully

                        p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());

                        Clickable_Join.sendMessage(target,
                                "/f join " + faction.name,
                                Messages.invited_by.language(p).setPlayer(p).queue(),
                                Messages.hover_join.language(p).queue());

                        //target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                        //Invite kiírása a faction számára
                        //Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));

                        for (Player member : faction.getMembers()) {
                            member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                        }

                        faction.inviteHistory.add(0, new HistoryEntrys.InviteEntry(
                                p.getName(),
                                target.getName(),
                                new Date().getTime(),
                                true
                        ));
                    } else {
                        // This player is already invited
                        p.sendMessage(Messages.already_invited.language(p).queue());
                    }
                } else {
                    p.sendMessage(Messages.player_in_faction.queue());
                }
            } else {
                p.sendMessage(Messages.not_found_player.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_invited.language(p).queue());
        }
    }
}
