package me.idbi.hcf;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HCF_Rules {
    public final static ArrayList<Material> blacklistedBlocks = new ArrayList<Material>() {{
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
        add(Material.FURNACE);
        add(Material.BURNING_FURNACE);
    }};
    public final static ArrayList<Material> usableBlacklist = new ArrayList<Material>() {{
        add(Material.CHEST);
        add(Material.TRAPPED_CHEST);
        add(Material.FURNACE);
        add(Material.BURNING_FURNACE);
    }};
    public final static HashMap<PotionEffectType, Integer> PotionLimits = new HashMap<PotionEffectType, Integer>() {{
        put(PotionEffectType.INCREASE_DAMAGE, 2);
        put(PotionEffectType.SPEED, 4);
        put(PotionEffectType.REGENERATION, 2);
        put(PotionEffectType.DAMAGE_RESISTANCE, 3);
        put(PotionEffectType.ABSORPTION, 2);
        put(PotionEffectType.JUMP, 3);
    }};
    public final static HashMap<Enchantment, Integer> allowedLevels = new HashMap<>() {{
        put(Enchantment.ARROW_INFINITE, 1);
        put(Enchantment.ARROW_KNOCKBACK, 2);
        put(Enchantment.ARROW_DAMAGE, -1);
        put(Enchantment.ARROW_FIRE, -1);
        put(Enchantment.DURABILITY, -1);
        put(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
        put(Enchantment.DAMAGE_ALL, 2);
        put(Enchantment.FIRE_ASPECT, 1);
        put(Enchantment.KNOCKBACK, 2);
        put(Enchantment.DIG_SPEED, -1);
        put(Enchantment.PROTECTION_FALL, 3);
        put(Enchantment.SILK_TOUCH, -1);
        // put(CustomEnchants.COMBO, -1);
    }};
    public static final String startMessage = " _    _   _____  ______        _                        _            _ \n" +
            "| |  | | / ____||  ____| _    | |                      | |          | |\n" +
            "| |__| || |     | |__  _| |_  | |      ___    __ _   __| |  ___   __| |\n" +
            "|  __  || |     |  __||_   _| | |     / _ \\  / _` | / _` | / _ \\ / _` |\n" +
            "| |  | || |____ | |     |_|   | |____| (_) || (_| || (_| ||  __/| (_| |\n" +
            "|_|  |_| \\_____||_|           |______|\\___/  \\__,_| \\__,_| \\___| \\__,_|\n" +
            "                                                                       ";
    public static final String startMessage2 = "  _    _    _    _    _    _    _     _    _    _     _    _    _    _     _    _    _     _    _    _    _  \n" +
            " / \\  / \\  / \\  / \\  / \\  / \\  / \\   / \\  / \\  / \\   / \\  / \\  / \\  / \\   / \\  / \\  / \\   / \\  / \\  / \\  / \\ \n" +
            "( C )( r )( e )( a )( t )( e )( d ) ( b )( y )( : ) ( K )( o )( b )( a ) ( a )( n )( d ) ( I )( d )( b )( i )\n" +
            " \\_/  \\_/  \\_/  \\_/  \\_/  \\_/  \\_/   \\_/  \\_/  \\_/   \\_/  \\_/  \\_/  \\_/   \\_/  \\_/  \\_/   \\_/  \\_/  \\_/  \\_/";
    public static final int maxInvitesPerFaction = 14; // >> GUI limit
    public static final int maxMembersPerFaction = 14; // >> GUI limit
    public static final int maxRanksPerFaction = 28; // >> GUI limit
    public static final int maxAlliesPerFaction = 14; // >> GUI limit
    public static final String VERSION = "0.1";
    public static final String AUTHORS = "Idbi & Koba";
    public static final String DISCORD = "https://discord.gg/aaswGgjJgU";
    public static final String WEBSITE = "UwU";
    public static final String startMessageInfo = "\n" +
            "-------------------------------------------\n" +
            "HCF Plus | Version" + VERSION + "\n" +
            ">>Authors: " + AUTHORS + "\n" +
            ">>Discord: " + DISCORD + "\n" +
            ">>Website: " + WEBSITE + "\n" +
            "-------------------------------------------";
    public static ImmutableSet blockedPearlTypes;

    /*
        Protection
        Sharpness
        FireAspect
        Feather Falling
        Durability
        Flame
        Infinity
        Punch
        Knockback
        Power
        Efficiency
        Silk Touch
     */
    public static Enchant_Obj randomEnchant() {
        double a = Math.random();
        int c;//Enchantment
        Enchant_Obj enchant_obj = null;
        if (a <= 0.1) { // 10%
            //Infinity 1
            //Fire Aspect 2
            //Silk Touch 3
            //Flame 4
            c = new Random().nextInt(5);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_INFINITE, 1);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.FIRE_ASPECT, 1);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.SILK_TOUCH, 1);
                case 3 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_FIRE, 1);
                case 4 -> enchant_obj = new Enchant_Obj(Enchantment.getById(100), 1);
            }
        } else if (a <= 0.5) {
            //Efficiency
            //Punch
            //Knockback
            c = new Random().nextInt(6);
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.DIG_SPEED, lvl);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_DAMAGE, lvl);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.KNOCKBACK, lvl);
                case 3 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_KNOCKBACK, lvl);
                case 4 -> enchant_obj = new Enchant_Obj(Enchantment.DURABILITY, lvl);
                case 5 -> enchant_obj = new Enchant_Obj(Enchantment.getById(100), lvl);
            }
        } else {
            //Protection
            //Sharpness
            //Feather falling
            c = new Random().nextInt(4);
            //Math.random() * (max - min + 1) + min
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.PROTECTION_ENVIRONMENTAL, lvl);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.DAMAGE_ALL, lvl);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.PROTECTION_FALL, lvl);
                case 3 -> enchant_obj = new Enchant_Obj(Enchantment.getById(100), lvl);
            }
        }
        return enchant_obj;
    }

    public static void setupPearlGlitch() {
        blockedPearlTypes = Sets.immutableEnumSet((Enum) Material.THIN_GLASS,
                (Enum[]) new Material[]{
                        Material.IRON_FENCE, Material.FENCE, Material.NETHER_FENCE,
                        Material.FENCE_GATE, Material.ACACIA_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.BRICK_STAIRS,
                        Material.COBBLESTONE_STAIRS, Material.DARK_OAK_STAIRS, Material.JUNGLE_WOOD_STAIRS,
                        Material.NETHER_BRICK_STAIRS, Material.QUARTZ_STAIRS, Material.SANDSTONE_STAIRS,
                        Material.SMOOTH_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.WOOD_STAIRS});
    }

    public static class Enchant_Obj {
        private final Enchantment enchantment;
        private final int level;

        Enchant_Obj(Enchantment enc, int level) {
            this.enchantment = enc;
            this.level = level;
        }

        public Enchantment getEnchantment() {
            return this.enchantment;
        }

        public int getLevel() {
            return this.level;
        }
    }


}
