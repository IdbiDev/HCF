package me.idbi.hcf.InventoryRollback.GUI;

import me.idbi.hcf.FactionGUI.Items.GUI_Items;
import me.idbi.hcf.InventoryRollback.Rollback;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PlayerRollbackListInventory {

    public static Inventory inv(Player owner, Player target, Rollback currentRollback) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(target);
        Inventory inv = Bukkit.createInventory(null, 6 * 9, target.getName() + "'s rollback #" + currentRollback.getId());


        inv.clear();
        List<Rollback> rollbacks = new ArrayList<>(hcfPlayer.getRollbacks().values());
        for (Rollback backs : rollbacks) {
            if (backs.getId() == currentRollback.getId()) {
                inv.setContents(currentRollback.getContents());
            }
        }

        for (int i = 36; i <= 44; i++) {
            inv.setItem(i, GUI_Items.blackGlass());
        }

        int counter = 45 + Math.min(8, Math.max(0, rollbacks.size() - 1));
        for (int i = rollbacks.size() - 1; i >= 0; i--) {
            if (counter < 45) break;
            Rollback rollback = rollbacks.get(i);

            if (rollback.getId() == currentRollback.getId()) {
                //inv.setContents(currentRollback.getInventory().getContents());
                inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfoActive(owner, rollback));
                ArrayList<ItemStack> armorContentList = new ArrayList<ItemStack>(Arrays.asList(rollback.getArmorContents()));
                Collections.reverse(armorContentList);
                int innerCounter = 36;
                for (ItemStack armorContent : armorContentList) {
                    if (armorContent.getType() == Material.AIR) continue;
                    inv.setItem(innerCounter, armorContent);
                    innerCounter++;
                }
            } else {
                inv.setItem(counter, PlayerRollbackItems.getPlayerRollbackInfo(owner, rollback));
            }


            counter--;

        }
        return inv;
    }
}
