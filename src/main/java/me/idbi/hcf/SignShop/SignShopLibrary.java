package me.idbi.hcf.SignShop;

import me.idbi.hcf.Main;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SignShopLibrary {

    public static boolean isFull(Player p, ItemStack is) {
        if(p.getInventory().addItem(is).isEmpty())
            return false;
        return true;
    }
}
