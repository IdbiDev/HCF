package me.idbi.hcf.Tools;


import me.idbi.hcf.AdminSystem.AdminCommandManager;
import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandRegistry;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.Commands.*;
import me.idbi.hcf.Main;
import me.idbi.hcf.Reclaim.Commands.ReclaimCommand;

public class SetupCommands {
    public static void setupCommands() {
        Main m = Main.getPlugin(Main.class);
        m.getCommand("faction").setExecutor(new FactionCommandManager());
        //m.getCommand("faction").setExecutor(new faction());
        m.getCommand("admin").setExecutor(new AdminCommandManager());
        m.getCommand("ally").setExecutor(new AllyCommandManager());
        m.getCommand("fc").setExecutor(new FactionPositionCommand());
        m.getCommand("koth").setExecutor(new KothCommand());
        m.getCommand("setuplogs").setExecutor(new Command_test());
        m.getCommand("customtimer").setExecutor(new Command_customtimer());
        m.getCommand("revive").setExecutor(new ReviveCommand());
        m.getCommand("reclaim").setExecutor(new ReclaimCommand());
        m.getCommand("logout").setExecutor(new LogoutCommand());


        CommandRegistry.register(BukkitCommandManager.getCommands());
    }
}
