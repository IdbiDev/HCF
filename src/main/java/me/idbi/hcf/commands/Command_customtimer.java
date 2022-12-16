package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.CustomTimers;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.commands.CustomTimer.CT_Create;
import me.idbi.hcf.commands.CustomTimer.CT_Remove;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class Command_customtimer implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("customtimer")) {
            if (sender instanceof Player p) {
                if(p.hasPermission("factions.admin.customtimer")) {
                    if(args[0].equalsIgnoreCase("create")) {
                        CT_Create.createCustomTimer(p, args);
                    }
                    else  if(args[0].equalsIgnoreCase("delete")) {
                        CT_Remove.removeCustomTimer(p, args);
                    }
                    else if(args[0].equalsIgnoreCase("settime")) {
                    }
                    else if(args[0].equalsIgnoreCase("settext")) {
                    }
                    else if(args[0].equalsIgnoreCase("list")) {
                        if(Main.customSBTimers.isEmpty()) {
                            p.sendMessage(Messages.CUSTOMT_NO_ACTIVE_TIMER.queue());
                            return false;
                        }
                        p.sendMessage(" ");
                        for (Map.Entry<String, CustomTimers> cus : Main.customSBTimers.entrySet()) {
                            p.sendMessage("§e" + cus.getKey() + " §a(" + (cus.getValue().isActive() ? "§aActive§a" : "§cExpired§a")
                                    + ")§7: §r" + cus.getValue().text);
                            p.sendMessage("§aExpire in: §2" + Scoreboards.ConvertTime(((int) (cus.getValue().getTime() / 1000))));
                            p.sendMessage(" ");
                        }
                    }
                }
            }
        }
        return false;
    }
}
