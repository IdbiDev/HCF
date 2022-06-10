package me.idbi.hcf.Elevator;

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

        if (line0 != null) {
            if (line0.equalsIgnoreCase("[elevator]")) {
                e.setLine(0, "§9[Elevator]");
            }

            if (line1.equalsIgnoreCase("up")) {
                e.setLine(1, "Up");
            } else if (line1.equalsIgnoreCase("down")) {
                e.setLine(1, "Down");
            }
        }
    }
}
