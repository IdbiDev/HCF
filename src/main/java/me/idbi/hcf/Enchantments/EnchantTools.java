package me.idbi.hcf.Enchantments;

import me.idbi.hcf.CustomFiles.EnchantmentFile;
import org.bukkit.Material;

import java.util.ArrayList;

public class EnchantTools {

    public static String formatLevel(int level) {
        switch (level) {
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            default:
                return "I";
        }
    }

    public static boolean isEnabled(String name) {
        return EnchantmentFile.get().getBoolean(name + ".enabled");
    }

    public static ArrayList<Material> getSwords() {
        return new ArrayList<Material>() {{
            add(Material.WOOD_SWORD);
            add(Material.GOLD_SWORD);
            add(Material.STONE_SWORD);
            add(Material.DIAMOND_SWORD);
            add(Material.IRON_SWORD);
        }};
    }

    public static ArrayList<Material> getBow() {
        return new ArrayList<Material>() {{
            add(Material.BOW);
        }};
    }

    public static ArrayList<Material> getFishingRod() {
        return new ArrayList<Material>() {{
            add(Material.FISHING_ROD);
        }};
    }

    public static ArrayList<Material> getArmors() {
        return new ArrayList<Material>() {{
            add(Material.LEATHER_BOOTS);
            add(Material.LEATHER_LEGGINGS);
            add(Material.LEATHER_CHESTPLATE);
            add(Material.LEATHER_HELMET);

            add(Material.CHAINMAIL_BOOTS);
            add(Material.CHAINMAIL_LEGGINGS);
            add(Material.CHAINMAIL_CHESTPLATE);
            add(Material.CHAINMAIL_HELMET);

            add(Material.GOLD_BOOTS);
            add(Material.GOLD_LEGGINGS);
            add(Material.GOLD_CHESTPLATE);
            add(Material.GOLD_HELMET);

            add(Material.IRON_BOOTS);
            add(Material.IRON_LEGGINGS);
            add(Material.IRON_CHESTPLATE);
            add(Material.IRON_HELMET);

            add(Material.DIAMOND_BOOTS);
            add(Material.DIAMOND_LEGGINGS);
            add(Material.DIAMOND_CHESTPLATE);
            add(Material.DIAMOND_HELMET);
        }};
    }
    public static ArrayList<Material> getTools() {
        return new ArrayList<Material>() {{
            add(Material.WOOD_AXE);
            add(Material.WOOD_PICKAXE);
            add(Material.WOOD_HOE);
            add(Material.WOOD_SPADE);

            add(Material.STONE_AXE);
            add(Material.STONE_PICKAXE);
            add(Material.STONE_HOE);
            add(Material.STONE_SPADE);

            add(Material.IRON_AXE);
            add(Material.IRON_PICKAXE);
            add(Material.IRON_HOE);
            add(Material.IRON_SPADE);

            add(Material.GOLD_AXE);
            add(Material.GOLD_PICKAXE);
            add(Material.GOLD_HOE);
            add(Material.GOLD_SPADE);

            add(Material.DIAMOND_AXE);
            add(Material.DIAMOND_PICKAXE);
            add(Material.DIAMOND_HOE);
            add(Material.DIAMOND_SPADE);
        }};
    }
}
