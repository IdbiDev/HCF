package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.entity.Player;

public class CT_Remove {

    public static void removeCustomTimer(Player p, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete")) {
                if (CustomTimers.isCreated(args[1])) {
                    Main.customSBTimers.get(args[1]).delete();
                    p.sendMessage(Messages.customt_deleted.language(p).queue());
                } else {
                    p.sendMessage(Messages.customt_not_found.language(p).queue());
                }
            }
        }
    }
}
