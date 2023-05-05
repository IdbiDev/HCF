package me.idbi.hcf.Commands.SingleCommands;

import me.idbi.hcf.BukkitCommands.CommandInfo;
import me.idbi.hcf.BukkitCommands.HCFCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.InventoryRollback.GUI.PlayerRollbackListInventory;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
