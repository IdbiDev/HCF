package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Faction_Invite {
    public static void InvitePlayerToFaction(Player p, String name) {
        if (!playertools.getMetadata(p, "factionid").equalsIgnoreCase("0")) {
            if (!playertools.hasPermission(p, rankManager.Permissions.INVITE)) {
                //Todo nincs jog
                p.sendMessage(Messages.NO_PERMISSION.queue());
                return;
            }
            Player target = Bukkit.getPlayer(name);
            if (target != null) {
                if (playertools.getMetadata(target, "factionid").equals("0")) {
                    Main.Faction faction = Main.faction_cache.get(Integer.valueOf(playertools.getMetadata(p, "factionid")));

                    if (!faction.isPlayerInvited(target)) {
                        faction.invitePlayer(target);
                        // Broadcast both player the invite success full

                        p.sendMessage(Messages.INVITED_PLAYER.repPlayer(target).queue());
                        target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                        //Invite kiírása a faction számára
                        Main.Faction f = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
                        f.BroadcastFaction(Messages.FACTION_INVITE_BROADCAST.repExecutor(p).repPlayer(target).queue());

                    } else {
                        // This player is already invited
                        p.sendMessage(Messages.ALREADY_INVITED.queue());
                    }
                } else {
                    p.sendMessage(Messages.PLAYER_IN_FACTION.queue());
                }
            } else {
                p.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
            }
        } else {
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
}
