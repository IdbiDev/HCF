package me.idbi.hcf.BukkitCommands;

import me.idbi.hcf.BukkitCommands.UtilCommands.BackCommand;
import me.idbi.hcf.BukkitCommands.UtilCommands.GodCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.idbi.hcf.BukkitCommands.HCFCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BukkitCommandManager {

    private static List<HCFCommand> cmds = new ArrayList<HCFCommand>();

    public BukkitCommandManager() {
        cmds.add(new BackCommand());
        cmds.add(new GodCommand());
    }

    public static List<HCFCommand> getCommands() {
        return cmds;
    }

    public static void setupMaps() {
        BackCommand.hashMap = new HashMap<>();
        GodCommand.hashMap = new ArrayList<>();
    }

    public static boolean findTarget(CommandSender executor, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if(!target.isOnline()) {
            if(executor instanceof Player) {
                executor.sendMessage(Messages.not_found_player.language((Player) executor).queue());
            } else {
                executor.sendMessage(Messages.not_found_player.queue());
            }
            return false;
        }

        return true;
    }

    public static Player getTarget(String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if(target.isOnline()) {
            return target.getPlayer();
        }

        return null;
    }
}
