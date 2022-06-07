package me.idbi.hcf;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

public class HCF_Rules {
    public static ArrayList blacklistedBlocks = new ArrayList() {{
        add(Material.LEVER);
        add(Material.STONE_BUTTON);
        add(Material.WOODEN_DOOR);
        add(Material.WOOD_BUTTON);
        add(Material.WOOD_DOOR);
        add(Material.TRAP_DOOR);
        add(Material.FENCE_GATE);
        add(Material.BIRCH_FENCE_GATE);
        add(Material.BIRCH_DOOR);
        add(Material.SPRUCE_DOOR);
        add(Material.SPRUCE_FENCE_GATE);
        add(Material.JUNGLE_DOOR);
        add(Material.JUNGLE_FENCE_GATE);
        add(Material.ACACIA_DOOR);
        add(Material.ACACIA_FENCE_GATE);
        add(Material.DARK_OAK_DOOR);
        add(Material.DARK_OAK_FENCE_GATE);
        add(Material.CHEST);
        add(Material.TRAPPED_CHEST);
        add(Material.ENDER_CHEST);
        add(Material.HOPPER);
        add(Material.BREWING_STAND);
    }};
    public static HashMap<PotionEffectType, Integer> PotionLimits = new HashMap(){{
        put(PotionEffectType.INCREASE_DAMAGE,2);
        put(PotionEffectType.SPEED,4);
        put(PotionEffectType.REGENERATION,2);
        put(PotionEffectType.DAMAGE_RESISTANCE,3);
        put(PotionEffectType.ABSORPTION,2);
        put(PotionEffectType.JUMP,3);
    }};

}
