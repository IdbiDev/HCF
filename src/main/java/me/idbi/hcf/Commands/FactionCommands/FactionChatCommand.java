package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

public class FactionChatCommand extends SubCommand {
    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction chat <text>";
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("chat")) {
                if (Playertools.getPlayerFaction(p) == null) {
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
            if (!args[0].equalsIgnoreCase("chat")) return;
            if (Playertools.getPlayerFaction(p) == null) {
                p.sendMessage(Messages.not_in_faction.language(p).queue());
                return;
            }

            Faction faction = Playertools.getPlayerFaction(p);
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
