package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.entity.Player;

public class CT_EditTime {


    public static void editCustomTimerTime(Player p, String[] args) {
        long seconds = System.currentTimeMillis();
        if (args.length == 3) {
            if (!CustomTimers.isCreated(args[1])) {
                p.sendMessage(Messages.customt_not_found.language(p).queue());
                return;
            }

            if (args[2].endsWith("h")) {
                if (args[2].replace("h", "").matches("^[0-9]+$")) {
                    seconds += Integer.parseInt(args[2].replace("h", "")) * 3600 * 1000L;
                } else {
                    p.sendMessage("§cUsage: /customtimer settime <name> <time>h|m");
                    return;
                }
            }
            //
            else if (args[2].endsWith("m")) {
                if (args[2].replace("m", "").matches("^[0-9]+$")) {
                    seconds += Integer.parseInt(args[2].replace("m", "")) * 60 * 1000L;
                } else {
                    p.sendMessage("§cUsage: /customtimer settime <name> <time>h|m");
                    return;
                }
            }

            CustomTimers timer = Main.customSBTimers.get(args[1]);
            timer.setTime(seconds);
            p.sendMessage(Messages.customt_edited.language(p).queue());
        }
    }
}
