package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Scoreboard.CustomTimers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CT_Create {


    public static void createCustomTimer(Player p, String[] args) {
        long seconds = System.currentTimeMillis();
        if (args.length == 4) {
            if (CustomTimers.isCreated(args[1])) {
                p.sendMessage(Messages.already_created.language(p).queue());
                return;
            }
            if (args[2].endsWith("h")) {
                if (args[2].replace("h", "").matches("^[0-9]+$")) {
                    seconds += Integer.parseInt(args[2].replace("h", "")) * 3600 * 1000L;
                } else {
                    p.sendMessage("§cUsage: /customtimer create <name> <time>h|m <text_with_underline>");
                    return;
                }
            }
            //
            else if (args[2].endsWith("m")) {
                if (args[2].replace("m", "").matches("^[0-9]+$")) {
                    seconds += Integer.parseInt(args[2].replace("m", "")) * 60 * 1000L;
                } else {
                    p.sendMessage("§cUsage: /customtimer create <name> <time>h|m <text_with_underline>");
                    return;
                }
            }
            String mainText = ChatColor.translateAlternateColorCodes('&', args[3].replace("_", " "));

            new CustomTimers(args[1], (seconds), mainText);

            p.sendMessage(Messages.customt_created.language(p).queue());
        }
    }
}
