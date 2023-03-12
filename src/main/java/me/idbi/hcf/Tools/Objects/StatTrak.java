package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.CustomFiles.Configs.Config;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.*;

public class StatTrak {


    public static void addStatTrak(Player killer, Player victim) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        TimeZone timezone = TimeZone.getTimeZone(Config.Timezone.asStr());
        formatter.setTimeZone(timezone);

        ItemStack killerSword = killer.getItemInHand();
        if(killerSword == null) return;
        if(Config.StatTrakTrackingItems.asStrList().contains(killerSword.getType().name())) {
            int kills = getKills(killerSword);
            List<String> currentLore = new ArrayList<>();
            int newKills = addKills(killerSword);
            ItemMeta im = killerSword.getItemMeta();
            if(im.hasLore()) {
                currentLore = im.getLore();
            }

            String killCounter = Config.StatTrakKillFormat.asStr().replace("%kills%", newKills + "");
            String killStatTrak = Config.StatTrakKillString.asStr()
                    .replace("%player%", victim.getName())
                    .replace("%killer%", killer.getName())
                    .replace("%date%", formatter.format(new Date()));

            if(kills > 0) {
                currentLore.set(currentLore.size() - kills - 1, killCounter);
                currentLore.add(currentLore.size() - kills, killStatTrak);
            } else {
                currentLore.add(" ");
                currentLore.add(killCounter);
                currentLore.add(killStatTrak);
            }

            im.setLore(currentLore);
            killerSword.setItemMeta(im);
            return;
        }
    }

    public static int getKills(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("kill_counter");
        return compString;
    }

    public static int addKills(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("kill_counter");
        // Save section
        compound.setInt("kill_counter", compString + 1);
        isNMS.setTag(compound);
        is.setItemMeta(CraftItemStack.asBukkitCopy(isNMS).getItemMeta());
        return compString + 1;
    }
}
