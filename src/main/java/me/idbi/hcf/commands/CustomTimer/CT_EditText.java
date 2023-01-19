package me.idbi.hcf.commands.CustomTimer;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.Scoreboard.CustomTimers;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CT_EditText {

    public static void editCustomTimerText(Player p, String[] args) {
        if (args.length == 3) {
            if(!CustomTimers.isCreated(args[1])) {
                p.sendMessage(Messages.CUSTOMT_NOT_FOUND.queue());
                return;
            }
            CustomTimers timer = Main.customSBTimers.get(args[1]);
            String mainText = ChatColor.translateAlternateColorCodes('&', args[2].replace("_", " "));
            timer.setText(mainText);
            p.sendMessage(Messages.CUSTOMT_EDITED.queue());
        }
    }
}
