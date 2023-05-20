package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.ChatTypes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionChatCommand extends SubCommand {
    @Override
    public String getName() {
        return "chat";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("c");
    }

    @Override
    public String getDescription() {
        return "Write to the selected chat";
    }

    @Override
    public String getSyntax() {
        return "/faction chat <public | staff | faction | ally | leader>";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (args.length == 1) {
            p.sendMessage("§cUsage: " + getSyntax());
/*            if (args[0].equalsIgnoreCase("chat")) {
                if (Playertools.getPlayerFaction(p) == null) {
                    p.sendMessage(Messages.not_in_faction.language(p).queue());
                    return;
                }
                ChatTypes newChat = ChatTypes.getByName(args[1]);
                if(newChat == null) {

                    p.sendMessage("");
                    return;
                }
                boolean toggle = hcfPlayer.factionChat;
                addCooldown(p);
                if (toggle) {
                    p.sendMessage(Messages.faction_chat_toggle_off.language(p).queue());
                } else {
                    p.sendMessage(Messages.faction_chat_toggle_on.language(p).queue());
                }
                hcfPlayer.factionChat = !hcfPlayer.factionChat;*/
            //}
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("chat")) {
                ChatTypes newChat = ChatTypes.getByName(args[1]);
                if (newChat == null) {
                    p.sendMessage(Messages.chat_unavailable.language(p).queue());
                    return;
                }

                if (newChat != ChatTypes.STAFF && newChat != ChatTypes.PUBLIC) {
                    if (Playertools.getPlayerFaction(p) == null) {
                        p.sendMessage(Messages.not_in_faction.language(p).queue());
                        return;
                    }
                }
                hcfPlayer.setChatType(newChat);
                p.sendMessage(Messages.chat_channel_changed.language(p).setChat(p).queue());
//            }
//            if (Playertools.getPlayerFaction(p) == null) {
//                p.sendMessage(Messages.not_in_faction.language(p).queue());
//                return;
//            }

                //addCooldown(p);

            /*Faction faction = Playertools.getPlayerFaction(p);
            faction.BroadcastFaction(Messages.faction_chat
                    .setMessage(
                            String.join(" ", args).replace(args[0] + " ", ""))
                    .setPlayer(p)
                    .replace("%rank%", hcfPlayer.rank.name)
                    .setFaction(faction.name)
            );*/
                // faction chat ally arg2 arg3
            }
        } else if (args.length > 2) {
            if (args[0].equalsIgnoreCase("chat")) {
                ChatTypes newChat = ChatTypes.getByName(args[1]);
                if (newChat == null) {
                    // ToDo Message: This chat channel is not available!
                    p.sendMessage(Messages.chat_unavailable.language(p).queue());
                    return;
                }

                if (newChat != ChatTypes.STAFF && newChat != ChatTypes.PUBLIC) {
                    if (Playertools.getPlayerFaction(p) == null) {
                        p.sendMessage(Messages.not_in_faction.language(p).queue());
                        return;
                    }
                }

                //addCooldown(p);
                hcfPlayer.sendChat(String.join(" ", args)
                        .replace(args[0] + " ", "")
                        .replaceFirst(args[1] + " ", ""), newChat);
                    /*Faction faction = Playertools.getPlayerFaction(p);
                    Messages message = null;
                    switch (newChat) {
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
                            .setFaction(faction.name));*/
            }
        }
    }
}
