package me.idbi.hcf.commands;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.CustomTimers;
import me.idbi.hcf.Scoreboard.Scoreboards;
import me.idbi.hcf.commands.CustomTimer.CT_Create;
import me.idbi.hcf.commands.CustomTimer.CT_EditText;
import me.idbi.hcf.commands.CustomTimer.CT_EditTime;
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
                    if(args.length == 0) {
                        p.sendMessage("§cUsage: /customtimer help <create | delete | settext | settime>");
                        return false;
                    }
                    if(args[0].equalsIgnoreCase("create")) {
                        CT_Create.createCustomTimer(p, args);
                    }
                    else  if(args[0].equalsIgnoreCase("delete")) {
                        CT_Remove.removeCustomTimer(p, args);
                    }
                    else if(args[0].equalsIgnoreCase("settime")) {
                        CT_EditTime.editCustomTimerTime(p, args);
                    }
                    else if(args[0].equalsIgnoreCase("settext")) {
                        CT_EditText.editCustomTimerText(p, args);
                    }
                    else if(args[0].equalsIgnoreCase("list")) {
                        if(Main.customSBTimers.isEmpty()) {
                            p.sendMessage(Messages.customt_no_active_timer.language(p).queue());
                            return false;
                        }
                        p.sendMessage(" ");
                        int counter = 1;
                        for (Map.Entry<String, CustomTimers> cus : Main.customSBTimers.entrySet()) {
                            p.sendMessage("§3#" + counter + " §b" + cus.getKey() + " §7(" + (cus.getValue().isActive() ? "§aActive§7" : "§cExpired§7")
                                    + ")§7: §r" + cus.getValue().text);
                            p.sendMessage("§7§o(( Expire in §b" + Scoreboards.ConvertTime(((int) (cus.getValue().getTime() / 1000))) + "§7§o ))");
                            p.sendMessage(" ");
                            counter++;
                        }
                    } else if(args[0].equalsIgnoreCase("help")) {
                        if(args.length == 2) {
                            switch (args[1]) {
                                case "create" -> p.sendMessage("§cUsage: /customtimer create <name> <time>h|m <text_with_underline>");
                                case "delete" -> p.sendMessage("§cUsage: /customtimer delete <name>");
                                case "settext" -> p.sendMessage("§cUsage: /customtimer settext <name> <text>");
                                case "settime" -> p.sendMessage("§cUsage: /customtimer settime <name> <time>h|m");
                            }
                        } else if(args.length == 1) {
                            p.sendMessage("§cCommands:");
                            p.sendMessage("§e/customtimer create <name> <time>h|m <text_with_underline> §7- Create custom timer");
                            p.sendMessage("§e/customtimer delete <name> §7- Delete custom timer");
                            p.sendMessage("§e/customtimer settext <name> <text> §7- Modify the text to the new text");
                            p.sendMessage("§e/customtimer settime <name> <time>h|m §7- Sets the time to the new time");
                            p.sendMessage("§c/customtimer list §7- Lists the custom timers");
                        }
                    }
                }
            }
        }
        return false;
    }
}
