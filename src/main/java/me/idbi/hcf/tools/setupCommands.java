package me.idbi.hcf.tools;


import me.idbi.hcf.Main;
import me.idbi.hcf.commands.*;

public class setupCommands {
    public static void setupCommands() {
        Main m = Main.getPlugin(Main.class);
        m.getCommand("faction").setExecutor(new faction());
        m.getCommand("admin").setExecutor(new admin());
        m.getCommand("fc").setExecutor(new fc_position());
        m.getCommand("koth").setExecutor(new koth());
        m.getCommand("setuplogs").setExecutor(new Command_test());
        m.getCommand("customtimer").setExecutor(new Command_customtimer());
    }
}
