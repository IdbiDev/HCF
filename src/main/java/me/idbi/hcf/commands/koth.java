package me.idbi.hcf.commands;

import me.idbi.hcf.Main;
import me.idbi.hcf.koth.GUI.KOTHInventory;
import me.idbi.hcf.koth.GUI.KOTHItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class koth implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p){
            if (args.length > 0) {
                if (p.hasPermission("factions.admin.admin")) {
                    switch (args[0]) {
                        case "kothreward":
                            if(Main.kothRewardsGUI.contains(p.getUniqueId())) return false;
                            p.openInventory(KOTHInventory.kothInventory());
                            break;
                        case "claim":
                            KOTHItemManager.addRewardsToPlayer(p);
                            break;

                    }
                }
            }
        }
        return false;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (cmd.getName().equalsIgnoreCase("koth")) {
            if (args.length == 1) {
                return Arrays.asList(
                        "kothreward",
                        "getreward"
                );
            }
        }
        return null;
    }
}
