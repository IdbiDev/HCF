package me.idbi.hcf.Enchantments;

import me.idbi.hcf.CustomFiles.EnchantmentFile;
import me.idbi.hcf.Enchantments.Enchantments.PersistentTool;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class LoreTool {

    public static void updateLore(ItemStack is) {
        Map<Enchants, Integer> enchants = PersistentTool.getEnchants(is);

        if(is == null) return;
        if(!is.hasItemMeta()) return;
        ItemMeta im = is.getItemMeta();

        Map<Enchants, Integer> map = new TreeMap<>(Collections.reverseOrder());
        map.putAll(enchants);
        List<String> newLore = new ArrayList<>();

        if(im.hasLore())
            for (String s : im.getLore())
                if(!lineIsEnchant(s))
                    newLore.add(s);

        for (Map.Entry<Enchants, Integer> hash : map.entrySet())
            newLore.add(0, formatLine(hash.getKey(), hash.getValue()));

        im.setLore(newLore);
        is.setItemMeta(im);
    }

    public static String formatLine(Enchants enc, int level) {
        String config = EnchantmentFile.get().getString(enc.getName() + ".displayName");
        return ChatColor.translateAlternateColorCodes('&', config.replace("%level%", String.valueOf(level)));
    }

    private static boolean lineIsEnchant(String line) {
        line = ChatColor.stripColor(line);
        for (String s : EnchantmentFile.get().getKeys(false)) {
            String dName = EnchantmentFile.get().getString(s + ".displayName").replace("%level%", ""); // ne nézzé fasz
            if(line.startsWith(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', dName))))
                return true;
        }

        return false;
    }
}
