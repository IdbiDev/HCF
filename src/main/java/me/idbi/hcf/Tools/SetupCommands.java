package me.idbi.hcf.Tools;


import me.idbi.hcf.AdminSystem.AdminCommandManager;
import me.idbi.hcf.Commands.*;
import me.idbi.hcf.Main;

public class SetupCommands {
    public static void setupCommands() {
        Main m = Main.getPlugin(Main.class);
        m.getCommand("faction").setExecutor(new FactionCommandManager());
        //m.getCommand("faction").setExecutor(new faction());
        m.getCommand("admin").setExecutor(new AdminCommandManager());
        m.getCommand("ally").setExecutor(new AllyCommandManager());
        m.getCommand("fc").setExecutor(new fc_position());
        m.getCommand("koth").setExecutor(new koth());
        m.getCommand("setuplogs").setExecutor(new Command_test());
        m.getCommand("customtimer").setExecutor(new Command_customtimer());

    }
}
