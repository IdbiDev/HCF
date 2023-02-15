package me.idbi.hcf.events;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onPlayerChat implements Listener {
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if(Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "staffchat"))) {
            e.setCancelled(true);

            if(e.getMessage().split(" ").length == 2)
                if (e.getMessage().split(" ")[1].equalsIgnoreCase("chat"))
                    return;

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("factions.admin")) {
                    onlinePlayer.sendMessage(Messages.staff_chat.language(e.getPlayer()).setPlayer(e.getPlayer()).setMessage(e.getMessage()).queue());
                }
            }
            return;
        }

        if (Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "factionchat"))) {
            if(playertools.getMetadata(e.getPlayer(), "factionid").equals("0")) {
                e.getPlayer().sendMessage(Messages.not_in_faction.queue());
                return;
            }
            e.setCancelled(true);
            Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")));
            if(faction == null){
                e.getPlayer().sendMessage(Messages.not_in_faction.queue());
                return;
            }
            String msg = Messages.faction_chat
                    .setMessage(e.getMessage())
                    .setPlayer(e.getPlayer())
                    .setFaction(faction)
                    .replace("%rank%", playertools.getMetadata(e.getPlayer(), "rank")).queue();

            faction.BroadcastFaction(msg);
        } else {
            if(!playertools.getMetadata(e.getPlayer(), "factionid").equals("0")) {
                Faction faction = playertools.getPlayerFaction(e.getPlayer());
                String msg = e.getMessage();
                //e.setCancelled(true);
                e.setFormat(Messages.chat_prefix_faction
                        .setFaction(faction)
                        .setMessage(msg)
                        .setPlayer(e.getPlayer())
                        .setDisplayName(e.getPlayer())
                        .queue()
                );
            }else{
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
