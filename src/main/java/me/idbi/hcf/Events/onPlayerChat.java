package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.ChatTypes;
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
        Player p = e.getPlayer();
        /*if (player.hasStaffChat()) {
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
        }*/

        player.sendChat(e.getMessage(), player.chatType);
        e.setCancelled(true);
       /* if (player.chatType != ChatTypes.PUBLIC) {
            Messages message = null;
            switch (player.chatType) {
                case ALLY -> message = Messages.ally_chat.language(p);
                case LEADER -> message = Messages.leader_chat.language(p);
                case FACTION -> message = Messages.faction_chat.language(p);
                case STAFF -> message = Messages.staff_chat.language(p);
                case PUBLIC -> message = Messages.chat_prefix_faction.language(p);
            }
            faction.BroadcastFaction(message
                    .setMessage(
                            String.join(" ", args)
                                    .replace(args[0] + " ", "")
                                    .replaceFirst(args[1] + " ", ""))
                    .setPlayer(p)
                    .setRank(hcfPlayer.rank.name)
                    .setFaction(faction.name));
            if (!player.inFaction()) {
                e.getPlayer().sendMessage(Messages.not_in_faction.language(e.getPlayer()).queue());
                e.getPlayer().sendMessage(Messages.faction_chat_toggle_off.language(e.getPlayer()).queue());
                player.setFactionChat(false);
                e.setCancelled(true);
                return;
            }
            e.setCancelled(true);
            Faction faction = player.faction;
            if (faction == null) {
                e.getPlayer().sendMessage(Messages.not_in_faction.language(e.getPlayer()).queue());
                e.getPlayer().sendMessage(Messages.faction_chat_toggle_off.language(e.getPlayer()).queue());
                player.setFactionChat(false);
                e.setCancelled(true);
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
*/

    }
}
