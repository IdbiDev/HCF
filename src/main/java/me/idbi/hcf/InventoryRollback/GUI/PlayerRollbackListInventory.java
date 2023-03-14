package me.idbi.hcf.InventoryRollback.GUI;

import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Objects.PlayerObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerRollbackListInventory {

    public static Inventory inv(Player owner, Player target, Rollback currentRollback) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(target);
        Inventory inv = Bukkit.createInventory(null, 6*9, target.getName() + "'s rollback #" + currentRollback.getId());

        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        boolean isFirst = true;
        int counter = 45;
        for (Rollback rollback : hcfPlayer.getRollbacks().values()) {
            if(counter > 53) break;
            if(currentRollback == null) {
                if (isFirst) {
                    inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfoActive(owner, rollback));
                    int innerCounter = 36;
                    for (ItemStack armorContent : rollback.getInventory().getArmorContents()) {
                        inv.setItem(innerCounter, armorContent);
                        innerCounter++;
                    }
                } else {
                    inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfo(owner, rollback));
                }
                inv.setContents(rollback.getInventory().getContents());
                isFirst = false;
            } else {
                if(rollback.getId() == currentRollback.getId()) {
                    inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfoActive(owner, rollback));
                    int innerCounter = 36;
                    for (ItemStack armorContent : rollback.getInventory().getArmorContents()) {
                        inv.setItem(innerCounter, armorContent);
                        innerCounter++;
                    }
                } else {
                    inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfo(owner, rollback));
                }
                inv.setContents(currentRollback.getInventory().getContents());
            }
            counter++;
        }

        return inv;
    }
}
