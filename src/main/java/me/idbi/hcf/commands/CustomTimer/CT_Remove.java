package me.idbi.hcf.commands.CustomTimer;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.CustomTimers;
import org.bukkit.entity.Player;

public class CT_Remove {

    public static void removeCustomTimer(Player p, String[] args) {
        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("delete")) {
                if(CustomTimers.isCreated(args[1])) {
                    Main.customSBTimers.get(args[1]).delete();
                    p.sendMessage(Messages.CUSTOMT_DELETED.queue());
                } else {
                    p.sendMessage(Messages.CUSTOMT_NOT_FOUND.queue());
                }
            }
        }
    }
}
