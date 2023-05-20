package me.idbi.hcf.Commands.CustomTimer;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CT_EditText {

    public static void editCustomTimerText(Player p, String[] args) {
        if (args.length == 3) {
            if (!CustomTimers.isCreated(args[1])) {
                p.sendMessage(Messages.customt_not_found.language(p).queue());
                return;
            }
            CustomTimers timer = Main.customSBTimers.get(args[1]);
            String mainText = ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " "));
            timer.setText(mainText);
            p.sendMessage(Messages.customt_edited.language(p).queue());
        }
    }
}
