package me.idbi.hcf.Elevator;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class ElevatorCreate implements Listener {

    @EventHandler
    public void onCreateElevator(SignChangeEvent e) {
        String line0 = e.getLine(0);
        String line1 = e.getLine(1);

//        if(e.getLine(2) != null && e.getLine(3 )!= null) {
//            return;
//        }

        if(!Config.ElevatorEnabled.asBoolean()) return;
        String title = Config.ElevatorTitle.asStr();
        if (line0 != null) {
            if(!HCFPermissions.elevator_create.check(e.getPlayer())) return;
            if (line0.equalsIgnoreCase(ChatColor.stripColor(title)) || line0.equalsIgnoreCase(title)) {
                e.setLine(0, title);
            }

            if (line1.equalsIgnoreCase("up")) {
                e.setLine(1, "Up");
            } else if (line1.equalsIgnoreCase("down")) {
                e.setLine(1, "Down");
            }
        }
    }
}
