package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onPlayerChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        HCFPlayer player = HCFPlayer.getPlayer(e.getPlayer());
        if (player.staffChat) {
            e.setCancelled(true);

            if (e.getMessage().split(" ").length == 2)
                if (e.getMessage().split(" ")[1].equalsIgnoreCase("chat"))
                    return;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("factions.admin")) {
                    onlinePlayer.sendMessage(Messages.staff_chat.language(e.getPlayer()).setPlayer(e.getPlayer()).setMessage(e.getMessage()).queue());
                }
            }
            return;
        }

        if (player.factionChat) {
            if (player.inFaction()) {
                e.getPlayer().sendMessage(Messages.not_in_faction.queue());
                return;
            }
            e.setCancelled(true);
            Faction faction = player.faction;
            if (faction == null) {
                e.getPlayer().sendMessage(Messages.not_in_faction.queue());
                return;
            }
            String msg = Messages.faction_chat
                    .setMessage(e.getMessage())
                    .setPlayer(e.getPlayer())
                    .setFaction(faction)
                    .setRank(player.rank.name).queue();

            faction.BroadcastFaction(msg);
        } else {
            if (player.inFaction()) {
                Faction faction = Playertools.getPlayerFaction(e.getPlayer());
                String msg = e.getMessage();
                //e.setCancelled(true);
                e.setFormat(Messages.chat_prefix_faction
                        .setFaction(faction)
                        .setMessage(msg)
                        .setPlayer(e.getPlayer())
                        .setDisplayName(e.getPlayer())
                        .queue()
                );
            } else {
                String msg = e.getMessage();
                //e.setCancelled(true);
                e.setFormat(Messages.chat_prefix_without_faction
                        .setMessage(msg)
                        .setPlayer(e.getPlayer())
                        .setDisplayName(e.getPlayer())
                        .queue()
                );

            }

        }

    }
}
