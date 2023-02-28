package me.idbi.hcf.Koth.GUI;

import me.idbi.hcf.CustomFiles.KothRewardsFile;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KOTHItemManager {

    public static void saveItems(ArrayList<ItemStack> items) {
        ArrayList<ItemStack> itemsList = new ArrayList<>();
        for (ItemStack item : items) {
            itemsList.add(item);
        }

        KothRewardsFile.get().set("Rewards", itemsList);
        KothRewardsFile.save();
    }

    public static void addRewardsToPlayer(Player winner) {
        ArrayList<ItemStack> itemsList = (ArrayList<ItemStack>) KothRewardsFile.get().getList("Rewards");
        if (itemsList == null) return;
        if (itemsList.isEmpty()) return;

        HashMap<Integer, ItemStack> nope = winner.getInventory().addItem(itemsList.toArray(new ItemStack[0]));
        for (Map.Entry<Integer, ItemStack> entry : nope.entrySet()) {
            winner.getWorld().dropItemNaturally(winner.getLocation(), entry.getValue());
        }
    }

    public static ItemStack[] getRewards() {
        ArrayList<ItemStack> itemsList = (ArrayList<ItemStack>) KothRewardsFile.get().getList("Rewards");
        if (itemsList == null) return null;
        if (itemsList.isEmpty()) return null;

        return itemsList.toArray(new ItemStack[0]);
    }
}
