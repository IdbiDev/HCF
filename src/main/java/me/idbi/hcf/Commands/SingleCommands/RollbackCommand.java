package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.BukkitCommandManager;
import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.GUI.PlayerRollbackListInventory;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.MountainEvent;
import me.idbi.hcf.Tools.Playertools;
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
        syntax = "/rollback <player> [id]")
public class RollbackCommand extends HCFCommand {



    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void execute(Player p, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target.isOnline()) {
            HCFPlayer hcfPlayer = HCFPlayer.getPlayer(target.getPlayer());
            if (hcfPlayer.getRollbacks().isEmpty()) {
                p.sendMessage(Messages.rollback_dont_have_rollback.language(p).queue());
                return;
            }
            if(args.length == 2) {
                if (args[1].matches("^[0-9]+$")) {
                    int id = Integer.parseInt(args[1]);
                    try {
                        p.openInventory(PlayerRollbackListInventory.inv(p, target.getPlayer(), hcfPlayer.getRollback(id)));
                    } catch (NullPointerException e) {
                        p.sendMessage(Messages.rollback_not_found.language(p).queue());
                        p.openInventory(PlayerRollbackListInventory.inv(p, target.getPlayer(), hcfPlayer.getLastRollback()));
                    }
                    return;
                }
            }
            p.openInventory(PlayerRollbackListInventory.inv(p, target.getPlayer(), hcfPlayer.getLastRollback()));
            addCooldown(p);
        } else
            p.sendMessage(Messages.not_found_player.language(p).queue());
    }
}
