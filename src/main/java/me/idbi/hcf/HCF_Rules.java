package me.idbi.hcf;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class HCF_Rules {
    public final static ArrayList blacklistedBlocks = new ArrayList() {{
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
    public final static HashMap<PotionEffectType, Integer> PotionLimits = new HashMap() {{
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
    }};

    public final static HashMap<Integer, String> DTR_MEMBERS = new HashMap() {{
        //Member / Max DTR
        put(0, 1.5);
        put(1, 1.5);
        put(2, 2.0);
        put(3, 3.5);
        put(4, 4.0);
        put(5, 5.5);
    }};
    public static class Enchant_Obj{
        private Enchantment enchantment;
        private int level;
        Enchant_Obj(Enchantment enc,int level){
            this.enchantment = enc;
            this.level = level;
        }
        public Enchantment getEnchantment(){
            return this.enchantment;
        }
        public int getLevel(){
            return this.level;
        }
    }
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
    public static Enchant_Obj randomEnchant(){
        double a = Math.random();
        int c;//Enchantment
        Enchant_Obj enchant_obj = null;
        if(a<=0.1){ // 10%
            //Infinity 1
            //Fire Aspect 2
            //Silk Touch 3
            //Flame 4
            c = new Random().nextInt(4);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_INFINITE, 1);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.FIRE_ASPECT, 1);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.SILK_TOUCH, 1);
                case 3 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_FIRE, 1);
            }
        } else if(a<=0.5){
            //Efficiency
            //Punch
            //Knockback
            c = new Random().nextInt(5);
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.DIG_SPEED, lvl);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_DAMAGE, lvl);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.KNOCKBACK, lvl);
                case 3 -> enchant_obj = new Enchant_Obj(Enchantment.ARROW_KNOCKBACK, lvl);
                case 4 -> enchant_obj = new Enchant_Obj(Enchantment.DURABILITY, lvl);
            }
        }else{
            //Protection
            //Sharpness
            //Feather falling
            c = new Random().nextInt(3);
            //Math.random() * (max - min + 1) + min
            int lvl = (int) (Math.random() * (2 - 1 + 1) + 1);
            switch (c) {
                case 0 -> enchant_obj = new Enchant_Obj(Enchantment.PROTECTION_ENVIRONMENTAL, lvl);
                case 1 -> enchant_obj = new Enchant_Obj(Enchantment.DAMAGE_ALL, lvl);
                case 2 -> enchant_obj = new Enchant_Obj(Enchantment.PROTECTION_FALL, lvl);
            }
        }
        return enchant_obj;
    }


}
