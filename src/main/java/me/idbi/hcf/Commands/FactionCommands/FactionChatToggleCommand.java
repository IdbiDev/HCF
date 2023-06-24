package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.ChatTypes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

public class FactionChatToggleCommand extends SubCommand {

    @Override
    public String getName() {
        return "togglechat";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) || argument.equalsIgnoreCase("toggle");
    }

    @Override
    public String getDescription() {
        return "Toggles the selected chat";
    }

    @Override
    public String getSyntax() {
        return "/faction togglechat <public | staff | faction | ally | leader>";
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
                p.sendMessage(Messages.chat_unavailable.language(p).queue());
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
