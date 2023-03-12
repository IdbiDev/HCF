package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.CustomFiles.Configs.Config;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Crowbar {

    public static ItemStack is() {
        ItemStack is = new ItemStack(Material.getMaterial(Config.CrowbarMaterial.asStr()));
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(Config.CrowbarName.asStr());
        List<String> configList = Config.CrowbarLore.asChatColorList();
        List<String> lore = new ArrayList<>();
        for (String s : configList) {
            lore.add(s.replace("%pcount%", 0 + "").replace("%scount%", 0 + ""));
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static int getPortalUses(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("portal_uses");
        return compString;
    }

    public static int addPortalUses(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("portal_uses");
        // Save section
        compound.setInt("portal_uses", compString + 1);
        isNMS.setTag(compound);
        is.setItemMeta(CraftItemStack.asBukkitCopy(isNMS).getItemMeta());
        ItemMeta im = is.getItemMeta();
        List<String> configList = Config.CrowbarLore.asChatColorList();
        List<String> lore = new ArrayList<>();
        for (String s : configList) {
            lore.add(s.replace("%pcount%", compString + 1 + "").replace("%scount%", getSpawnerUses(is) + ""));
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return compString + 1;
    }

    public static int getSpawnerUses(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("spawner_uses");
        return compString;
    }

    public static int addSpawnerUses(ItemStack is) {
        net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();
        int compString = compound.getInt("spawner_uses");
        // Save section
        compound.setInt("spawner_uses", compString + 1);
        isNMS.setTag(compound);
        is.setItemMeta(CraftItemStack.asBukkitCopy(isNMS).getItemMeta());
        ItemMeta im = is.getItemMeta();
        List<String> configList = Config.CrowbarLore.asChatColorList();
        List<String> lore = new ArrayList<>();
        for (String s : configList) {
            lore.add(s.replace("%pcount%", getPortalUses(is) + "").replace("%scount%", compString + 1 + ""));
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return compString + 1;
    }
}
