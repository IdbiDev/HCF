package me.idbi.hcf.Tools;


import me.idbi.hcf.AdminSystem.AdminCommandManager;
import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandRegistry;
import me.idbi.hcf.Commands.*;
import me.idbi.hcf.Commands.SingleCommands.*;
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
        m.getCommand("koth").setExecutor(new KothCommandManager());
        m.getCommand("setuplogs").setExecutor(new Command_test());
        m.getCommand("customtimer").setExecutor(new CustomTimerCommand());
        m.getCommand("revive").setExecutor(new ReviveCommand());
        m.getCommand("reclaim").setExecutor(new ReclaimCommand());
        m.getCommand("logout").setExecutor(new LogoutCommand());
        m.getCommand("deleteconfig").setExecutor(new Command_test());
        m.getCommand("balance").setExecutor(new Command_test());
        m.getCommand("pvp").setExecutor(new PvpTimerCommand());
        m.getCommand("coordinates").setExecutor(new CoordinatesCommand());
        m.getCommand("mountain").setExecutor(new MountainCommand());
        m.getCommand("waypoint").setExecutor(new WaypointCommand());
        m.getCommand("language").setExecutor(new LanguageCommand());
        m.getCommand("customclaim").setExecutor(new CustomClaimCommandManager());
        m.getCommand("lives").setExecutor(new LivesCommandManager());
        m.getCommand("rollback").setExecutor(new RollbackCommand());
        m.getCommand("slots").setExecutor(new SlotsCommand());
        m.getCommand("lookingforfaction").setExecutor(new LookingFactionCommand());
        m.getCommand("setspawn").setExecutor(new SetspawnCommand());
        m.getCommand("spawn").setExecutor(new SpawnCommand());
        m.getCommand("endspawn").setExecutor(new EndspawnCommand());
        m.getCommand("netherspawn").setExecutor(new NetherspawnCommand());


        CommandRegistry.register(BukkitCommandManager.getCommands());
    }
}
