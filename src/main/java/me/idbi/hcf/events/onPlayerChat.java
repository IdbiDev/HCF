package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.neznamy.tab.api.placeholder.Placeholder;
import org.bukkit.ChatColor;
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

            playertools.sendStaffChat(ChatColor.translateAlternateColorCodes('&',
                    Messages.STAFF_CHAT.repPlayer(e.getPlayer()).setMessage(e.getMessage()).queue()
            ));
            return;
        }

        if (Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "factionchat"))) {
            if(playertools.getMetadata(e.getPlayer(), "factionid").equals("0")) {
                e.getPlayer().sendMessage(Messages.NOT_IN_FACTION.queue());
                return;
            }
            e.setCancelled(true);
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")));
            if(faction == null){
                e.getPlayer().sendMessage(Messages.NOT_IN_FACTION.queue());
                return;
            }
            String msg = Messages.FACTION_CHAT.setMessage(e.getMessage()).repPlayer(e.getPlayer()).setFaction(faction.name).queue();
            faction.BroadcastFaction(msg.replace("%rank%", playertools.getMetadata(e.getPlayer(), "rank")));
        }else{
            if(!playertools.getMetadata(e.getPlayer(), "factionid").equals("0")) {
                String faction = playertools.getMetadata(e.getPlayer(), "faction");
                String msg = e.getMessage();
                //e.setCancelled(true);
                e.setFormat(Messages.CHAT_PREFIX_FACTION
                        .setFaction(faction)
                        .setMessage(msg)
                        .repPlayer(e.getPlayer())
                        .setDisplayName(e.getPlayer())
                        .queue()
                );
            }else{
                String msg = e.getMessage();
                //e.setCancelled(true);
                e.setFormat(Messages.CHAT_PREFIX_WITHOUT_FACTION
                        .setMessage(msg)
                        .repPlayer(e.getPlayer())
                        .setDisplayName(e.getPlayer())
                        .queue()
                );

            }

        }

    }
}
