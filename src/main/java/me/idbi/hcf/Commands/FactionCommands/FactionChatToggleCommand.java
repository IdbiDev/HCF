package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.ChatTypes;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionChatToggleCommand extends SubCommand {

    @Override
    public String getName() {
        return "togglechat";
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
        return "/faction togglechat <staff | faction | ally | leader>";
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
        return 1;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(args.length != 2) {
            p.sendMessage("§cUsage: " + getSyntax());
            return;
        } else {
            ChatTypes newChat = ChatTypes.getByName(args[1]);
            if (newChat == null) {
                // ToDo Message: This chat channel is not available!
                p.sendMessage("Nem elérhető a channel!");
                return;
            }
            if(newChat == ChatTypes.PUBLIC) {
                p.sendMessage(Messages.cant_disable_chat.language(p).queue());
                return;
            }
            boolean chat = hcfPlayer.toggleChat(newChat);
            if(chat) {
                p.sendMessage(Messages.toggle_chat_enable.language(p).setChat(p, newChat).queue());
            } else {
                p.sendMessage(Messages.toggle_chat_disable.language(p).setChat(p, newChat).queue());
            }
            addCooldown(p);
        }
    }
}
