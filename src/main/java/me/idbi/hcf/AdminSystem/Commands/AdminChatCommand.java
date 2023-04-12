package me.idbi.hcf.AdminSystem.Commands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.AdminScoreboard;
import me.idbi.hcf.Tools.Objects.ChatTypes;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AdminChatCommand extends SubCommand {
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
        return null;
    }

    @Override
    public String getSyntax() {
        return "/admin " + getName() + " [on | off | message]";
    }

    @Override
    public String getPermission() {
        return "factions.command.admin." + getName();
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
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!Playertools.isInStaffDuty(p)) {
            p.sendMessage(Messages.not_in_duty.language(p).queue());
            return;
        }
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (args.length == 1) {
            if (isCommand(args[0])) {
                if (!player.hasStaffChat()) {
                    player.setChatType(ChatTypes.STAFF);
                    p.sendMessage(Messages.chat_channel_changed.language(p).setChat(p).queue());
                } else {
                    player.setChatType(ChatTypes.PUBLIC);
                    p.sendMessage(Messages.chat_channel_changed.language(p).setChat(p).queue());
                }
            }
            AdminScoreboard.refresh(p);
        } else if (args.length == 2) {
            if (isCommand(args[0])) {
                HCFPlayer hcf = HCFPlayer.getPlayer(p);
                if (args[1].equalsIgnoreCase("on")) {
                    player.setChatType(ChatTypes.STAFF);
                    p.sendMessage(Messages.chat_channel_changed.language(p).setChat(p).queue());
                } else if (args[1].equalsIgnoreCase("off")) {
                    player.setChatType(ChatTypes.PUBLIC);
                    p.sendMessage(Messages.chat_channel_changed.language(p).setChat(p).queue());
                } else {
                    List<String> argsList = Arrays.stream(args).toList();
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        if (hasPermission(p)) {
                            onlinePlayer.sendMessage(
                                    Messages.staff_chat.language(onlinePlayer)
                                            .setPlayer(p)
                                            .setMessage(getMessage(argsList)
                                                    .replaceFirst(args[0] + " ", ""))
                                            .queue());
                        }
                    }
                }
            }
            AdminScoreboard.refresh(p);
        } else {
            List<String> argsList = Arrays.stream(args).toList();
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (hasPermission(p)) {
                    onlinePlayer.sendMessage(Messages.staff_chat.language(onlinePlayer).setPlayer(p).setMessage(getMessage(argsList)).queue());
                }
            }
        }
    }

    public String getMessage(List<String> args) {
        String output = "";

        for (String s : args) {
            output += s;
            output += " ";
        }

        return output;
    }
}
