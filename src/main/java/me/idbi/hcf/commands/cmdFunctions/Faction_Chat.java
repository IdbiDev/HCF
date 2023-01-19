package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class Faction_Chat {

    public static void chat(Player p, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("chat")) {
                if(playertools.getMetadata(p, "factionid").equals("0")) {
                    p.sendMessage(Messages.NOT_IN_FACTION.queue());
                    return;
                }
                boolean toggle = Boolean.parseBoolean(playertools.getMetadata(p, "factionchat"));
                if (toggle) {
                    p.sendMessage(Messages.FACTION_CHAT_TOGGLE_OFF.queue());
                    playertools.setMetadata(p, "factionchat", false);
                } else {
                    p.sendMessage(Messages.FACTION_CHAT_TOGGLE_ON.queue());
                    playertools.setMetadata(p, "factionchat", true);
                }
            }
        } else if (args.length > 1) {
            if(!args[0].equalsIgnoreCase("chat")) return;
            if(playertools.getMetadata(p, "factionid").equals("0")) {
                p.sendMessage(Messages.NOT_IN_FACTION.queue());
                return;
            }

            Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));

            faction.BroadcastFaction(Messages.FACTION_CHAT
                    .setMessage(
                            String.join(" ", args).replace(args[0] + " ", ""))
                    .repPlayer(p)
                    .setFaction(faction.name)
                    .queue()
                    .replace("%rank%", playertools.getMetadata(p, "rank")));
        }
    }
}
