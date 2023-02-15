package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Objects.HCFPlayer;
import me.idbi.hcf.tools.playertools;
import org.bukkit.entity.Player;

public class Faction_Chat {

    public static void chat(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("chat")) {
                if(playertools.getPlayerFaction(p) == null) {
                    p.sendMessage(Messages.not_in_faction.language(p).queue());
                    return;
                }
                boolean toggle = hcfPlayer.factionChat;
                if (toggle) {
                    p.sendMessage(Messages.faction_chat_toggle_off.language(p).queue());
                } else {
                    p.sendMessage(Messages.faction_chat_toggle_on.language(p).queue());
                }
                hcfPlayer.factionChat = !hcfPlayer.factionChat;
            }
        } else if (args.length > 1) {
            if(!args[0].equalsIgnoreCase("chat")) return;
            if(playertools.getPlayerFaction(p) == null) {
                p.sendMessage(Messages.not_in_faction.language(p).queue());
                return;
            }

            Faction faction = playertools.getPlayerFaction(p);
            faction.BroadcastFaction(Messages.faction_chat
                    .setMessage(
                            String.join(" ", args).replace(args[0] + " ", ""))
                    .setPlayer(p)
                    .replace("%rank%", hcfPlayer.rank.name)
                    .setFaction(faction.name)
            );

        }
    }
}
