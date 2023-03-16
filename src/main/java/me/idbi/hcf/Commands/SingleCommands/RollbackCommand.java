package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.GUI.PlayerRollbackListInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@CommandInfo(
        name = "rollback",
        description = "Open rollback menu",
        permission = "factions.commands.admin.rollback",
        syntax = "/rollback <player>")
public class RollbackCommand  extends HCFCommand {

    @Override
    public void execute(Player p, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if(target.isOnline()) {
            p.openInventory(PlayerRollbackListInventory.inv(p, target.getPlayer(), null));
        } else
            p.sendMessage(Messages.not_found_player.language(p).queue());
    }
}
