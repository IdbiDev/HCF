package me.idbi.hcf.Enchantments.Enchantments;

import me.idbi.hcf.Enchantments.ETools.EnchantJson;
import me.idbi.hcf.Enchantments.Enchants;
import me.idbi.hcf.tools.JsonUtils;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.json.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class PersistentTool {

    public static void addEnchant(ItemStack is, Enchants.EnchantmentType enchant, int level) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();

        if(hasEnchant(is)) {
            // Add to json
            String compString = compound.getString("enchants");
            Map<String, Object> json = JsonUtils.jsonToMap(new JSONObject(compString));
            json.put(enchant.getName(), level);

            // Save section
            compound.setString("enchants", EnchantJson.getJSON(json).toString());
        } else {
            compound.setString("enchants", new JSONObject().put(enchant.getName(), level).toString());
        }
        isNMS.setTag(compound);
        is.setItemMeta(CraftItemStack.asBukkitCopy(isNMS).getItemMeta());
    }

    public static void removeEnchant(ItemStack is, Enchants.EnchantmentType enchant) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();

        if(hasEnchant(is)) {
            // Add to json
            String compString = compound.getString("enchants");
            Map<String, Object> json = JsonUtils.jsonToMap(new JSONObject(compString));
            if(json.containsKey(enchant.getName()))
                json.remove(enchant.getName());

            // Save section
            compound.setString("enchants", EnchantJson.getJSON(json).toString());
            isNMS.setTag(compound);
            is.setItemMeta(CraftItemStack.asBukkitCopy(isNMS).getItemMeta());
        }
    }

    public static boolean hasEnchant(ItemStack is) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        return isNMS.hasTag() && isNMS.getTag().hasKey("enchants");
    }

    public static Map<Enchants, Integer> getEnchants(ItemStack is) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();

        if(hasEnchant(is)) {
            String json = compound.getString("enchants");
            return EnchantJson.getEnch(new JSONObject(json));
        }

        return new HashMap<>();
    }

    public static JSONObject getEnchantsAsJson(ItemStack is) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        final NBTTagCompound compound = isNMS.hasTag() ? isNMS.getTag() : new NBTTagCompound();

        if(hasEnchant(is)) {
            String json = compound.getString("enchants");
            return new JSONObject(json);
        }

        return null;
    }



    public static String getNBT(ItemStack is) {
        final net.minecraft.server.v1_8_R3.ItemStack isNMS = CraftItemStack.asNMSCopy(is);
        if(!isNMS.hasTag()) {
            return "-1";
        }

        final NBTTagCompound compound = isNMS.getTag();
        return compound.hasKey("enchants") ? compound.getString("enchants") : "Nincs enchant";
    }
}
