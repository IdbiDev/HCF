package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class onPlayerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (Boolean.parseBoolean(playertools.getMetadata(e.getPlayer(), "factionchat"))) {
            e.setCancelled(true);
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(e.getPlayer(), "factionid")));

            String msg = Messages.FACTION_CHAT.setMessage(e.getMessage()).repPlayer(e.getPlayer()).setFaction(faction.factioname).queue();
            faction.BroadcastFaction(msg);
        }
    }
}
