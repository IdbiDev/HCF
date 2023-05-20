package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.CustomFiles.Configs.Config;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Wand {
    public static ItemStack claimWand() {
        ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
        ItemMeta meta = wand.getItemMeta();

        meta.setDisplayName(Config.ClaimingWandTitle.asStr());
        meta.setLore(Config.ClaimingWandLore.asChatColorList());

        wand.setItemMeta(meta);
        wand.setItemMeta(meta);
        return wand;
    }
}
